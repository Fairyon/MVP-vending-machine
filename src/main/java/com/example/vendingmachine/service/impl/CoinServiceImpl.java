package com.example.vendingmachine.service.impl;

import com.example.vendingmachine.dao.CoinRepository;
import com.example.vendingmachine.model.entity.Coin;
import com.example.vendingmachine.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoinServiceImpl implements CoinService {

    @Autowired
    private CoinRepository coinRepository;

    @Override
    public Coin saveCoin(Coin coin){
        return coinRepository.saveAndFlush(coin);
    }

    @Override
    public Coin findByValue(Integer value){
        return coinRepository.findByValue(value).orElse(null);
    }

    @Override
    public List<Coin> findAllCoins() {
        return coinRepository.findAll();
    }

    @Override
    public boolean deleteCoin(Long coinId) {
        try {
            coinRepository.deleteById(coinId);
            return true;
        } catch(EmptyResultDataAccessException e) {
            return false;
        }
    }
}
