package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.FieldOfExpertResponse;

public interface FieldOfExpertService {
    FieldOfExpertResponse getAllField() throws Exception;
    FieldOfExpertResponse getFieldByID( String id) throws Exception;
    FieldOfExpertResponse getByIdParentField(String id) throws Exception;
    FieldOfExpertResponse addFieldOfExpert(String nameField, String tags, String idParentField) throws Exception;
    FieldOfExpertResponse editFieldOfExpert( String id, String nameField, String tags, String idParentField) throws Exception;
    FieldOfExpertResponse deleteFieldOfExpert( String id) throws Exception;
}
