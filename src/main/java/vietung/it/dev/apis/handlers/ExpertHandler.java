package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.ExpertService;
import vietung.it.dev.core.services.imp.ExpertServiceImp;
import vietung.it.dev.core.utils.Utils;

public class ExpertHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        ExpertService service = new ExpertServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("editname")){
                String phone = request.getParam("ph");
                String nName = request.getParam("name");
                if(phone!=null && nName!=null){
                    return service.editnameExpert(phone,nName);
                }else{
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            }else if(type.equals("editphone")){
                String phone = request.getParam("ph");
                String nPhone = request.getParam("newphone");
                if(phone!=null && nPhone!=null){
                    return service.editPhoneExpert(phone,nPhone);
                }else{
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            }else if(type.equals("editemail")){
                String phone = request.getParam("ph");
                String email = request.getParam("email");
                if(phone!=null && email!=null){
                    return service.editEmailExpert(phone,email);
                }else{
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            }else if(type.equals("editaddress")){
                String phone = request.getParam("ph");
                String address = request.getParam("address");
                if(phone!=null && address!=null){
                    return service.editAddressExpert(phone,address);
                }else{
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            }else if(type.equals("edittags")){
                String phone = request.getParam("ph");
                String tags = request.getParam("tags");
                if(phone!=null){
                    if(tags==null || tags.trim().equals("")){
                        tags="[]";
                    }
                    return service.editTagsExpert(phone,tags);
                }else{
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            }else{
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
