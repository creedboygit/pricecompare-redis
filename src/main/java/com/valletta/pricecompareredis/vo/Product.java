package com.valletta.pricecompareredis.vo;

import lombok.Data;

@Data
public class Product {

    private String prodGrpId;
    private String productId;
    private int price;
}
