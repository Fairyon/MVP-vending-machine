package com.example.vendingmachine.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TradingProductData {

    @NotNull
    @Min(1)
    public Long productId;
    @NotNull
    @Min(1)
    public Integer amount;
}
