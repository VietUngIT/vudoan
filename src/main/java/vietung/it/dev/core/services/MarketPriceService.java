package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.MarketPriceResponse;

public interface MarketPriceService {
    MarketPriceResponse deleteMarketPrice(String id) throws Exception;

    MarketPriceResponse createMarketPrice(String idCate, String name, long price, String place, String unit, String note) throws Exception;

    MarketPriceResponse getAllTypeMarketPrice(int ofset) throws Exception;

    MarketPriceResponse getMarketPriceByCate(String idCate, int ofset, int page) throws Exception;

    MarketPriceResponse editMarketPrice(String id,String idCate, String name, String price, String place, String unit, String note) throws Exception;
}
