package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.FieldOfExpertService;
import vietung.it.dev.core.services.imp.FieldOfExpertServiceImp;
import vietung.it.dev.core.utils.Utils;

public class FieldOfExpertHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        FieldOfExpertService service = new FieldOfExpertServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("getall")){
                return service.getAllField();
            }else if(type.equals("get")){
                String strID = request.getParam("id");
                if(strID!=null){
                    try{
                        int id = Integer.parseInt(strID);
                        return service.getFieldByID(id);
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
