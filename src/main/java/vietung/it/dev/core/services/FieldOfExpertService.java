package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.FieldOfExpertResponse;

public interface FieldOfExpertService {
    FieldOfExpertResponse getAllField();
    FieldOfExpertResponse getFieldByID( int id);
    FieldOfExpertResponse addFieldOfExpert( String nameField);
    FieldOfExpertResponse editFieldOfExpert( int id, String nameField);
    FieldOfExpertResponse deleteFieldOfExpert( int id);
}
