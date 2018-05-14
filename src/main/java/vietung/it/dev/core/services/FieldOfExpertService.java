package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.FieldOfExpertResponse;

import java.util.List;

public interface FieldOfExpertService {
    FieldOfExpertResponse getAllField() throws Exception;
    FieldOfExpertResponse getFieldByID( String id) throws Exception;
    FieldOfExpertResponse getByIdParentField(String id) throws Exception;
    FieldOfExpertResponse addFieldOfExpert(String nameField, String tags, String idParentField) throws Exception;
    FieldOfExpertResponse editFieldOfExpert( String id, String nameField, String tags, String idParentField) throws Exception;
    FieldOfExpertResponse deleteFieldOfExpert( String id) throws Exception;
    List<String> getListFieldMatchTags( List<String> tags) throws Exception;
}
