package com.valletta.pricecompareredis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valletta.pricecompareredis.vo.Keyword;
import com.valletta.pricecompareredis.vo.NotFoundException;
import com.valletta.pricecompareredis.vo.Product;
import com.valletta.pricecompareredis.vo.ProductGrp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LowestPriceServiceImpl implements LowestPriceService {

    @Autowired
    private RedisTemplate myProdPriceRedis;

    public Set getZsetValue(String key) {
        Set myTempSet = new HashSet();
        myTempSet = myProdPriceRedis.opsForZSet().rangeWithScores(key, 0, 9);
        return myTempSet;
    }

    public Set getZsetValueWithStatus(String key) throws Exception {
        Set myTempSet = new HashSet();
        myTempSet = myProdPriceRedis.opsForZSet().rangeWithScores(key, 0, 9);
        if (myTempSet.size() < 1) {
            throw new Exception("the key doesn't have any member");
        }
        return myTempSet;
    }

    public int setNewProduct(Product newProduct) {
        int rank = 0;
        myProdPriceRedis.opsForZSet().add(newProduct.getProdGrpId(), newProduct.getProductId(), newProduct.getPrice());
        rank = myProdPriceRedis.opsForZSet().rank(newProduct.getProdGrpId(), newProduct.getProductId()).intValue();

        return rank;
    }

    public int setNewProductGrp(ProductGrp newProductGrp) {
        List<Product> productList = newProductGrp.getProductList();
        String productId = productList.get(0).getProductId();
        int price = productList.get(0).getPrice();
        myProdPriceRedis.opsForZSet().add(newProductGrp.getProdGrpId(), productId, price);

        int productCnt = myProdPriceRedis.opsForZSet().zCard(newProductGrp.getProdGrpId()).intValue();
        return productCnt;
    }

    public int setNewProductGrpToKeyword(String keyword, String prodGrpId, double score) {
        myProdPriceRedis.opsForZSet().add(keyword, prodGrpId, score);
        int rank = myProdPriceRedis.opsForZSet().rank(keyword, prodGrpId).intValue();
        return rank;
    }

    public Keyword getLowestPriceProductByKeyword(String keyword) {
        Keyword returnInfo = new Keyword();
        List<ProductGrp> tempProductGrp = new ArrayList<>();

        // keyword를 통해 ProductGroup 가져오기 (10개)
        tempProductGrp = getProductGrpUsingKeyword(keyword);

        // 가져온 정보들을 Return할 Object에 넣기
        returnInfo.setKeyword(keyword);
        returnInfo.setProductGrpList(tempProductGrp);

        // 해당 Object return
        return returnInfo;
    }

    @Override
    public Set<String> getZsetValueWithSpecificException(String key) throws Exception {
        Set myTempSet = new HashSet();
        myTempSet = myProdPriceRedis.opsForZSet().rangeWithScores(key, 0, 9);
        if (myTempSet.size() < 1) {
            throw new NotFoundException("the key doesn't exist in redis", HttpStatus.NOT_FOUND);
        }
        return myTempSet;
    }

    private List<ProductGrp> getProductGrpUsingKeyword(String keyword) {
        List<ProductGrp> returnInfo = new ArrayList<>();

        ProductGrp tempProductGrp = new ProductGrp();

        // input 받은 keyword 로 productGrpId를 조회
        List<String> prodGrpIdList = new ArrayList<>();
        prodGrpIdList = List.copyOf(myProdPriceRedis.opsForZSet().reverseRange(keyword, 0, 9));
//        Product tempProduct = new Product();
        List<Product> tempProdList = new ArrayList<>();

        // 10개 prodGrpId로 loop
        for (final String prodGrpId : prodGrpIdList) {

            ProductGrp tempProdGrp = new ProductGrp();

            // Loop로 ProductGroupId로 Product:price 가져오기 (10개)
            Set prodAndPriceList = new HashSet();
            prodAndPriceList = myProdPriceRedis.opsForZSet().rangeWithScores(prodGrpId, 0, 9);
            Iterator<Object> prodPriceObj = prodAndPriceList.iterator();

            // Loop로 Product Object에 bind (10개)
            while (prodPriceObj.hasNext()) {
                ObjectMapper objMapper = new ObjectMapper();

                // {"value":1234}, {"score":10000}
                Map<String, Object> prodPriceMap = objMapper.convertValue(prodPriceObj.next(), Map.class);
                Product tempProduct = new Product();

                // Product Object에 bind
                tempProduct.setProductId(prodPriceMap.get("value").toString());
                tempProduct.setPrice(Double.valueOf(prodPriceMap.get("score").toString()).intValue());
                tempProduct.setProdGrpId(prodGrpId);

                tempProdList.add(tempProduct);
            }

            // 10개 product price 입력 완료
            tempProductGrp.setProdGrpId(prodGrpId);
            tempProductGrp.setProductList(tempProdList);
            returnInfo.add(tempProductGrp);
        }

        return returnInfo;
    }
}
