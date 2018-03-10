package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.SimpleResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.TypeNewsService;
import vietung.it.dev.core.services.imp.TypeNewsServiceImp;

public class TypeNewsHandler extends BaseApiHandler{
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        TypeNewsService service = new TypeNewsServiceImp();
        String type = request.getParam("type");
        if(type != null){
            if(type.equals("getall")){

            }else if(type.equals("get")){

            }else {
                SimpleResponse response = new SimpleResponse();
                response.setError(ErrorCode.INVALID_PARAMS);
                response.setMsg("Invalid params.");
                return response;
            }
        }else {
            SimpleResponse response = new SimpleResponse();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }
        return null;
    }
}
