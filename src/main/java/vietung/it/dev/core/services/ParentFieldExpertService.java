package vietung.it.dev.core.services;

import org.jongo.Jongo;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.ParentFieldExpertResponse;
import vietung.it.dev.core.models.ParentFieldExpert;

import java.util.List;

public interface ParentFieldExpertService {
    ParentFieldExpertResponse getAllParentField() throws Exception;

    ParentFieldExpertResponse getParentFieldByID(String id) throws Exception;

    ParentFieldExpertResponse addParentFieldExpert(String nameField);

    ParentFieldExpertResponse editParentFieldExpert(String id, String nameField);

    ParentFieldExpertResponse deleteParentFieldExpert(String id) throws Exception;

    List<ParentFieldExpert> getListParentField(Jongo jongo) throws Exception;
}
