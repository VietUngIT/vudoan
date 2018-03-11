package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.PriceMarketResponse;

public interface PriceMarketService {
    PriceMarketResponse addPriceMarket(String namePlace,long price,String typeNews, String nameTypeNews);
    PriceMarketResponse editPriceMarket(String idPM,String namePlace,long price);
    PriceMarketResponse deletePriceMarket(String idPM);
    PriceMarketResponse getAllPriceMarket(String lastId,int ofset);
    PriceMarketResponse getPriceMarketByType(String idType,String lastId,int ofset);
}
