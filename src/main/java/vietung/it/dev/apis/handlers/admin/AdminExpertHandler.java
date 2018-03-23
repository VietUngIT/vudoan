package vietung.it.dev.apis.handlers.admin;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.handlers.BaseApiHandler;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.ExpertService;
import vietung.it.dev.core.services.imp.ExpertServiceImp;
import vietung.it.dev.core.utils.Utils;

public class AdminExpertHandler extends BaseApiHandler{
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        ExpertService service = new ExpertServiceImp();
        String type = request.getFormAttribute("t");
        if(type!=null){
            if(type.equals("ad")){
                String name = request.getFormAttribute("name");
                String phone = request.getFormAttribute("phone");
                String desc = request.getFormAttribute("desc");
                String email = request.getFormAttribute("email");
                String address = request.getFormAttribute("address");
                String location = request.getFormAttribute("location");
                String field = request.getFormAttribute("field");
                String tags = request.getFormAttribute("tags");
                String degree = request.getFormAttribute("degree");
                return addExpertHandle(name,phone,desc,email,address,location,field,tags,degree,service);
            }else if(type.equals("edit")){
                String phone = request.getFormAttribute("phone");
                String desc = request.getFormAttribute("desc");
                String location = request.getFormAttribute("location");
                String degree = request.getFormAttribute("degree");
                String field = request.getFormAttribute("field");
                String tags = request.getFormAttribute("tags");
                return editExpertHandle(phone,desc,location,degree,tags,field,service);
            } else if(type.equals("del")){
                String phone = request.getFormAttribute("id");
                return delExpertHandle(phone,service);
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse editExpertHandle(String phone, String desc, String location, String degree,String tags,String field, ExpertService service) throws Exception {
        if(phone!=null){
            return service.editExpert(phone,desc,location,degree,tags,field);
        }else{
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse delExpertHandle(String phone, ExpertService service) throws Exception {
        if(phone!=null){
            return service.deleteExpert(phone);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }


    private BaseResponse addExpertHandle(String name, String phone, String desc, String email, String address, String location, String field, String tags, String degree, ExpertService service) throws Exception {
        if(name!=null && phone!=null && desc!=null && email!=null && address!=null && location!=null && field!=null){
            if(tags==null){
                tags="[]";
            }
            if(degree==null){
                degree="[]";
            }
            return service.addExpert(name,phone,desc,email,address,location,field,tags,degree);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
