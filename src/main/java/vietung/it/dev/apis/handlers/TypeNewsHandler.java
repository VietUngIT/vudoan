package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.SimpleResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.TypeNewsService;
import vietung.it.dev.core.services.imp.TypeNewsServiceImp;
import vietung.it.dev.core.utils.Utils;

public class TypeNewsHandler extends BaseApiHandler{
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        TypeNewsService service = new TypeNewsServiceImp();
        String type = request.getParam("t");
        if(type != null){
            if(type.equals("getall")){
                String tc = request.getParam("typecate");
                if(tc!=null){
                    try {
                        int typeCate = Integer.parseInt(tc);
                        return service.getAllTypeNews(typeCate);
                    }catch (NumberFormatException e){
                        return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
                    }
                }else {
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }


            }else if(type.equals("get")){
                String id = request.getParam("id");
                return service.getTypeNews(id);
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
