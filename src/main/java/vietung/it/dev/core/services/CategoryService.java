package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategoryNews() throws Exception;
    CategoryResponse getCategoryNewsById(String id) throws Exception;
    CategoryResponse addCategoryNews(String name) throws Exception;
    CategoryResponse editCategoryNews(String id,String name) throws Exception;
    CategoryResponse deleteCategoryNews(String id) throws Exception;
}
