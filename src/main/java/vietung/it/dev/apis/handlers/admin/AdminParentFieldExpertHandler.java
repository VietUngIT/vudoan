package vietung.it.dev.apis.handlers.admin;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.handlers.BaseApiHandler;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.FieldOfExpertService;
import vietung.it.dev.core.services.ParentFieldExpertService;
import vietung.it.dev.core.services.imp.FieldOfExpertServiceImp;
import vietung.it.dev.core.services.imp.ParentFieldExpertServiceImp;
import vietung.it.dev.core.utils.Utils;

public class AdminParentFieldExpertHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        ParentFieldExpertService service = new ParentFieldExpertServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("add")){
                String nameField = request.getParam("name");
                if(nameField!=null){
                    return service.addParentFieldExpert(nameField);
                }else {
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            }else if(type.equals("edit")){
                String id = request.getParam("id");
                String nameField = request.getParam("name");
                if(id!=null && nameField!=null){
                    return service.editParentFieldExpert(id,nameField);
                }else {
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            }else if(type.equals("del")){
                String id = request.getParam("id");
                if(id!=null){
                    return service.deleteParentFieldExpert(id);
                }else {
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
