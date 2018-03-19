package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.SubCategoryResponse;

public interface SubCategoryService {
    SubCategoryResponse getAllSubCategoryAgritech() throws Exception;
    SubCategoryResponse getSubCategoryAgriTechByCate(String id) throws Exception;
    SubCategoryResponse getSubCategoryAgritechById(String id) throws Exception;
    SubCategoryResponse addSubCategoryAgritech(String idcate,String name) throws Exception;
    SubCategoryResponse editSubCategoryAgritech(String id, String name) throws Exception;
    SubCategoryResponse deleteSubCategoryAgritech(String id) throws Exception;
}
