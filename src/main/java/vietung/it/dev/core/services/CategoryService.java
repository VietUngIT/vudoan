package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategoryNews() throws Exception;
    CategoryResponse getCategoryNewsById(String id) throws Exception;
    CategoryResponse addCategoryNews(String name) throws Exception;
    CategoryResponse editCategoryNews(String id,String name) throws Exception;
    CategoryResponse deleteCategoryNews(String id) throws Exception;
    CategoryResponse getAllCategoryMarketInfo() throws Exception;
    CategoryResponse getCategoryMarketInfoById(String id) throws Exception;
    CategoryResponse addCategoryMarketInfo(String name) throws Exception;
    CategoryResponse editCategoryMarketInfo(String id,String name) throws Exception;
    CategoryResponse deleteCategoryMarketInfo(String id) throws Exception;
    CategoryResponse getAllCategoryAgriTech() throws Exception;
    CategoryResponse getCategoryAgriTechById(String id) throws Exception;
    CategoryResponse addCategoryAgriTech(String name) throws Exception;
    CategoryResponse editCategoryAgriTech(String id,String name) throws Exception;
    CategoryResponse deleteCategoryAgriTech(String id) throws Exception;
}
