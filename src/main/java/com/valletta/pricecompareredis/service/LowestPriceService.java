package com.valletta.pricecompareredis.service;

import com.valletta.pricecompareredis.vo.Keyword;
import com.valletta.pricecompareredis.vo.Product;
import com.valletta.pricecompareredis.vo.ProductGrp;
import java.util.Set;

public interface LowestPriceService {

    Set getZsetValue(String key);

    Set getZsetValueWithStatus(String key) throws Exception;

    int setNewProduct(Product newProduct);

    int setNewProductGrp(ProductGrp newProductGrp);

    int setNewProductGrpToKeyword(String keyword, String prodGrpId, double score);

    Keyword getLowestPriceProductByKeyword(String keyword);

    Set<String> getZsetValueWithSpecificException(String key) throws Exception;
}
