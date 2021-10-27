package com.example.vendingmachine.controller;

import com.example.vendingmachine.model.TradeReturnData;
import com.example.vendingmachine.model.entity.Coin;
import com.example.vendingmachine.model.entity.Product;
import com.example.vendingmachine.model.entity.User;
import com.example.vendingmachine.service.CoinService;
import com.example.vendingmachine.service.ProductService;
import com.example.vendingmachine.service.SecurityService;
import com.example.vendingmachine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class TradeController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@Valid Coin coin) {
        User user = securityService.findLoggedInUser();

        if(user == null)
            return new ResponseEntity<Object>("User does not exist.", HttpStatus.UNAUTHORIZED);
        if(user.getRole() != "buyer")
            return new ResponseEntity<Object>("User is not a buyer.", HttpStatus.UNAUTHORIZED);

        if(! IntStream.of(coin.POSSIBLE_VALUES).anyMatch(x -> x == coin.getValue()))
            return new ResponseEntity<Object>("Wrong coin value.", HttpStatus.NOT_ACCEPTABLE);

        final boolean[] coinFound = { false };

        Coin machineCoine = coinService.findByValue(coin.getValue());
        if(machineCoine != null){
            machineCoine.incrementAmountByOne();
            coinFound[0] = true;
        }

        if (!coinFound[0]) {
            coinService.saveCoin(coin);
            coinFound[0] = true;
        }

        if (coinFound[0]) {
            user.currentAmount += coin.getValue();
        }

        // update money
        this.userService.updateUser(user);

        return ResponseEntity.ok(user);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/buy")
    public ResponseEntity<?> buy(Long productId, Integer amountOfProducts) {
        User user = securityService.findLoggedInUser();

        if (user == null)
            return new ResponseEntity<Object>("User does not exist.", HttpStatus.UNAUTHORIZED);
        if (user.getRole() != "buyer")
            return new ResponseEntity<Object>("User is not a buyer.", HttpStatus.UNAUTHORIZED);

        Product product = productService.findById(productId);
        if(product == null)
            return new ResponseEntity<Object>("Product does not exist.", HttpStatus.NOT_FOUND);

        if(product.getCost() > user.currentAmount)
            return new ResponseEntity<Object>("Not enough money.", HttpStatus.NOT_ACCEPTABLE);

        Integer totalAmount = product.getCost() * amountOfProducts;

        if(totalAmount > user.currentAmount){
            Integer possibleToBuy = (int) (user.currentAmount / product.getCost());
            return new ResponseEntity<Object>("Not enough money to buy all products. " +
                    "Can buy only " + possibleToBuy + " products for your money", HttpStatus.NOT_ACCEPTABLE);
        }

        Integer changeAmount = user.currentAmount - totalAmount;

        final int[] restAmount = {0};

        Consumer<Integer> onImpossibleChange = parameter -> restAmount[0] = parameter;

        List<Coin> change = calcChange(changeAmount, onImpossibleChange);

        if(restAmount[0] != 0) {
            // todo give the user possible options to solve the problem
            return new ResponseEntity<Object>("Not enough coins for change. " +
                    "The rest amount of " + restAmount[0] + " cannot be changed", HttpStatus.NOT_ACCEPTABLE);
        }

        TradeReturnData result = new TradeReturnData();

        result.total = totalAmount;
        result.productId = productId;
        result.boughtProductsAmount = amountOfProducts;
        result.change = change;

        product.setAmountAvailable(product.getAmountAvailable() - amountOfProducts);
        productService.saveProduct(product);
        change.forEach(coin -> {
            Coin storedCoin = coinService.findByValue(coin.getValue());
            storedCoin.setAmount(storedCoin.getAmount() - coin.getAmount());
            coinService.saveCoin(storedCoin);
        });
        user.currentAmount -= totalAmount;

        return ResponseEntity.ok(result);
    }

    /**
     * /reset can be only called instead of successful buy. Because successfull buy returns all the money.
     * */

    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/reset")
    public ResponseEntity<?> reset() {
        User user = securityService.findLoggedInUser();

        if (user == null)
            return new ResponseEntity<Object>("User does not exist.", HttpStatus.UNAUTHORIZED);
        if (user.getRole() != "buyer")
            return new ResponseEntity<Object>("User is not a buyer.", HttpStatus.UNAUTHORIZED);

        final int[] restAmount = {0};
        List<Coin> change = calcChange(user.currentAmount, p -> restAmount[0] = p);

        if(restAmount[0] != 0){
            //todo Total refund cannot happen. Log it and inform user! Still we give back at least as much as we can.
        }

        user.currentAmount -= restAmount[0];

        return ResponseEntity.ok(change);
    }

    private List<Coin> calcChange(Integer changeAmount, Consumer<Integer> onImpossibleChange){
        List<Coin> refundCoins = new ArrayList<>();

        if(changeAmount == 0)
            return refundCoins;

        final int[] refundTotal = new int[1];

        refundTotal[0] = changeAmount;

        List<Coin> allCoins = coinService.findAllCoins();

        // descending sort
        allCoins.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        allCoins.forEach(coin -> {
            if (coin.getAmount() > 0 && coin.getValue() <= refundTotal[0]) {
                Integer maxCoins = Math.min((int) Math.floor(refundTotal[0] / coin.getValue()), coin.getAmount());
                refundCoins.add(new Coin(coin.getValue(), maxCoins));
                refundTotal[0] -= maxCoins * coin.getValue();
            }
        });

        if(refundTotal[0] != 0) {
            onImpossibleChange.accept(refundTotal[0]);
        }

        return refundCoins;
    }
}
