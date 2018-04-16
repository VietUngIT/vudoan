package vietung.it.dev.apis.handlers.admin;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.handlers.BaseApiHandler;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.FieldOfExpertService;
import vietung.it.dev.core.services.imp.FieldOfExpertServiceImp;
import vietung.it.dev.core.utils.Utils;

public class AdminFieldOfExpertHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        FieldOfExpertService service = new FieldOfExpertServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("add")){
                String idParentField = request.getParam("idparentfield");
                String nameField = request.getParam("name");
                String tags = request.getParam("tags");
                if(nameField!=null && idParentField!=null){
                    if(tags==null || tags.trim().equals("")){
                        tags="[]";
                    }
                    return service.addFieldOfExpert(nameField,tags,idParentField);
                }else {
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            }else if(type.equals("edit")){
                String id = request.getParam("id");
                String idParentField = request.getParam("idparentfield");
                String nameField = request.getParam("name");
                String tags = request.getParam("tags");
                if(id!=null && nameField!=null && idParentField!=null){
                    if(tags==null || tags.trim().equals("")){
                        tags="[]";
                    }
                    return service.editFieldOfExpert(id,nameField,tags, idParentField);
                }else {
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            }else if(type.equals("del")){
                String id = request.getParam("id");
                if(id!=null){
                    return service.deleteFieldOfExpert(id);
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
