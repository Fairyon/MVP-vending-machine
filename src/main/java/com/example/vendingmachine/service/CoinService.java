package com.example.vendingmachine.service;

import com.example.vendingmachine.model.entity.Coin;

import java.util.List;

public interface CoinService {

    Coin saveCoin(Coin coin);
    Coin findByValue(Integer value);
    List<Coin> findAllCoins();
    boolean deleteCoin(Long coinId);

}
