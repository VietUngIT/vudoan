package vietung.it.dev.core.services;

import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.ExpertResponse;
import vietung.it.dev.core.models.Expert;

import java.util.List;

public interface ExpertService {
    ExpertResponse addExpert(String name, String phone, String desc, String email, String address,String idParentField, Double lat,Double lon, String field, String tags, String degree, String workplace) throws Exception;

    ExpertResponse deleteExpert(String phone) throws Exception;

    ExpertResponse editExpert(String phone, String desc, Double lat,Double lon, String email, String idParentField, String workplace) throws Exception;

    ExpertResponse updateStatusOnlineExpert(Boolean isOnline,String id) throws Exception;

    ExpertResponse getInfoExpert(String id) throws Exception;

    ExpertResponse getListExpertNearest(Double lat, Double lon, int numExpert, String idparentfieldExpert) throws Exception;

    ExpertResponse editTagsExpert(String phone, String tags) throws Exception;

    ExpertResponse editDegreeExpert(String phone, String degree) throws Exception;

    ExpertResponse editFieldExpert(String phone, String idfield) throws Exception;

    ExpertResponse rateExpert(String phone,int rate) throws Exception;

    ExpertResponse getAllExpert(int ofs, int page) throws Exception;
    List<Expert> getListExpertByParentField(String idParentField) throws Exception;
}
