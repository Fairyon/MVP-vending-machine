package com.example.vendingmachine.model;

import com.example.vendingmachine.model.entity.Coin;

import java.util.List;

public class TradeReturnData {
    public Integer total;
    public Long productId;
    public Integer boughtProductsAmount;
    public List<Coin> change;
}
