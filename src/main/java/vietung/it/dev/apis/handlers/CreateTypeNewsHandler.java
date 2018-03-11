package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.SimpleResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.TypeNewsService;
import vietung.it.dev.core.services.imp.TypeNewsServiceImp;
import vietung.it.dev.core.utils.Utils;

public class CreateTypeNewsHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        TypeNewsService service = new TypeNewsServiceImp();
        String type = request.getFormAttribute("t");
        if(type != null){
            if(type.equals("ad")){
                String tc = request.getFormAttribute("typecate");
                String nameType = request.getFormAttribute("name");
                if(nameType!=null && tc!=null){
                    try{
                        int typeCate = Integer.parseInt(tc);
                        return service.addTypeNews(nameType,typeCate);
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                        return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu.");
                    }
                }else {
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            }else if(type.equals("ed")){
                String nameType = request.getFormAttribute("name");
                String id = request.getFormAttribute("id");
                if(nameType!=null && id!=null){
                    return service.editTypeNews(id,nameType);
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
