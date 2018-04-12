package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.ParentFieldExpertResponse;

public interface ParentFieldExpertService {
    ParentFieldExpertResponse getAllParentField() throws Exception;

    ParentFieldExpertResponse getParentFieldByID(String id) throws Exception;

    ParentFieldExpertResponse addParentFieldExpert(String nameField);

    ParentFieldExpertResponse editParentFieldExpert(String id, String nameField);

    ParentFieldExpertResponse deleteParentFieldExpert(String id);
}
