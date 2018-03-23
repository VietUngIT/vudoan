package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.CategoryResponse;
import vietung.it.dev.apis.response.FieldOfExpertResponse;

public interface FieldOfExpertService {
    CategoryResponse getAllField() throws Exception;
    CategoryResponse getFieldByID( String id) throws Exception;
    CategoryResponse addFieldOfExpert(String nameField) throws Exception;
    CategoryResponse editFieldOfExpert( String id, String nameField) throws Exception;
    CategoryResponse deleteFieldOfExpert( String id) throws Exception;
}
