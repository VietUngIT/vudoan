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
            if(type.equals("ad")){
                String nameField = request.getParam("name");
                if(nameField!=null){
                    return service.addFieldOfExpert(nameField);
                }else {
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            }else if(type.equals("ed")){
                String strId = request.getParam("id");
                String nameField = request.getParam("name");
                if(strId!=null && nameField!=null){
                    try{
                        int id = Integer.parseInt(strId);
                        return service.editFieldOfExpert(id,nameField);
                    }catch (NumberFormatException e){
                        return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu.");
                    }
                }else {
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            }else if(type.equals("del")){
                String strId = request.getParam("id");
                if(strId!=null){
                    try{
                        int id = Integer.parseInt(strId);
                        return service.deleteFieldOfExpert(id);
                    }catch (NumberFormatException e){
                        return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu.");
                    }
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
