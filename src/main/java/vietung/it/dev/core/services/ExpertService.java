package vietung.it.dev.core.services;

import com.google.gson.JsonObject;
import org.jongo.Jongo;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.ExpertResponse;
import vietung.it.dev.core.models.Expert;
import vietung.it.dev.core.models.Report;

import java.util.List;

public interface ExpertService {
    ExpertResponse addExpert(String name, String phone, String desc, String email, String address,String idParentField, String field, String tags, String degree, String workplace) throws Exception;

    ExpertResponse deleteExpert(String phone) throws Exception;

    ExpertResponse editExpert(String phone, String desc, Double lat,Double lon, String email, String idParentField, String workplace) throws Exception;

    ExpertResponse updateStatusOnlineExpert(Boolean isOnline,String id,Double lat,Double lon) throws Exception;

    ExpertResponse getInfoExpert(String id, String phone) throws Exception;

    ExpertResponse getListExpertNearest(Double lat, Double lon, int numExpert, String idparentfieldExpert) throws Exception;

    ExpertResponse editTagsExpert(String phone, String tags) throws Exception;

    ExpertResponse editDegreeExpert(String phone, String degree) throws Exception;

    ExpertResponse editFieldExpert(String phone, String idfield) throws Exception;

    ExpertResponse rateExpert(String id,int rate, String phone) throws Exception;

    ExpertResponse getAllExpert(int ofs, int page) throws Exception;

    List<Expert> getListExpertByParentField(String idParentField) throws Exception;

    List<Expert> getExpertByIds(List<String> ids) throws Exception;

    ExpertResponse getListExpertByIdField(String idparentfieldExpert,int ofs, int page) throws Exception;

    ExpertResponse statiticCommentByExpert(String id) throws Exception;

    Report gtExpertForDashBoard(Jongo jongo) throws Exception;
}
