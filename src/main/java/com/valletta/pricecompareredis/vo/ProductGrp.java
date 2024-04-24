package com.valletta.pricecompareredis.vo;

import java.util.List;
import lombok.Data;

@Data
public class ProductGrp {

    private String prodGrpId;
    private List<Product> productList;
}
