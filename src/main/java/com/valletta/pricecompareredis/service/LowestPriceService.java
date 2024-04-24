package com.valletta.pricecompareredis.service;

import com.valletta.pricecompareredis.vo.Product;
import java.util.Set;

public interface LowestPriceService {

    Set getZsetValue(String key);

    int setNewProduct(Product newProduct);
}
