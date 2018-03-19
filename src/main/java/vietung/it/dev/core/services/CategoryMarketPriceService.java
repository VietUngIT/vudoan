package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.CategoryMarketPriceResponse;

public interface CategoryMarketPriceService {
    CategoryMarketPriceResponse getAllCategoryMarketPrice() throws Exception;
    CategoryMarketPriceResponse getCategoryMarketPriceById(String id) throws Exception;
    CategoryMarketPriceResponse addCategoryMarketPrice(String name, String image) throws Exception;
    CategoryMarketPriceResponse editNameCategoryMarketPrice(String id, String name) throws Exception;
    CategoryMarketPriceResponse editImageCategoryMarketPrice(String id, String image) throws Exception;
    CategoryMarketPriceResponse deleteCategoryMarketPrice(String id) throws Exception;
}
