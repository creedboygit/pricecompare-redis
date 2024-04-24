package com.valletta.pricecompareredis.controller;

import com.valletta.pricecompareredis.service.LowestPriceService;
import com.valletta.pricecompareredis.vo.Product;
import com.valletta.pricecompareredis.vo.ProductGrp;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class LowestPriceController {

    @Autowired
    private LowestPriceService mlps;

    @GetMapping("/getZSETValue")
    public Set getZsetValue(String key) {
        return mlps.getZsetValue(key);
    }

    @PutMapping("/product")
    public int SetNewProduct(@RequestBody Product newProduct) {
        return mlps.setNewProduct(newProduct);
    }

    @PutMapping("/productGroup")
    public int SetNewProductGrp(@RequestBody ProductGrp newProductGrp) {
        return mlps.setNewProductGrp(newProductGrp);
    }
}
