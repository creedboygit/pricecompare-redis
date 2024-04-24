package com.valletta.pricecompareredis.vo;

import java.util.List;
import lombok.Data;

@Data
public class Keyword {

    private String keyword;

    private List<ProductGrp> productGrpList;
}
