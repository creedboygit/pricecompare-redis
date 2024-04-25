package com.valletta.pricecompareredis.controller;

import com.valletta.pricecompareredis.service.LowestPriceService;
import com.valletta.pricecompareredis.vo.Keyword;
import com.valletta.pricecompareredis.vo.NotFoundException;
import com.valletta.pricecompareredis.vo.Product;
import com.valletta.pricecompareredis.vo.ProductGrp;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/")
public class LowestPriceController {

    @Autowired
    private LowestPriceService mlps;

    @GetMapping("/product")
    public Set getZsetValue(String key) {
        return mlps.getZsetValue(key);
    }

    @GetMapping("/product1")
    public Set getZsetValueWithStatus(String key) {
        try {
            return mlps.getZsetValueWithStatus(key);
        } catch (Exception exception) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @GetMapping("/product2")
    public Set getZsetValueUsingExController(String key) throws Exception {
        try{
            return mlps.getZsetValueWithStatus(key);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    @GetMapping("/product3")
    public ResponseEntity<Set> getZsetValueUsinExControllerWithSpecificException(String key) throws Exception {
        Set<String> mySet = new HashSet<>();
        try {
            mySet = mlps.getZsetValueWithSpecificException(key);
        } catch (NotFoundException ex) {
            throw new Exception(ex);
        }
        HttpHeaders responseHeaders = new HttpHeaders();

        return new ResponseEntity<Set>(mySet, responseHeaders, HttpStatus.OK);
    }

    @PutMapping("/product")
    public int SetNewProduct(@RequestBody Product newProduct) {
        return mlps.setNewProduct(newProduct);
    }

    @PutMapping("/productGroup")
    public int SetNewProductGrp(@RequestBody ProductGrp newProductGrp) {
        return mlps.setNewProductGrp(newProductGrp);
    }

    @PutMapping("/productGrpToKeyword")
    public int SetNewProductGrpToKeyword(String keyword, String prodGrpId, double score) {
        return mlps.setNewProductGrpToKeyword(keyword, prodGrpId, score);
    }

    @GetMapping("/productPrice/lowest")
    public Keyword getLowestPriceProductByKeyword(String keyword) {
        return mlps.getLowestPriceProductByKeyword(keyword);
    }
}
