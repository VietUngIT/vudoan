package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.SimpleResponse;
import vietung.it.dev.core.consts.ErrorCode;

public class NewsHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("getall")){

            }else if(type.equals("get")){

            }else if(type.equals("cr")){

            }else if(type.equals("ed")){

            }else if(type.equals("del")){

            }else if(type.equals("comment")){

            }else if(type.equals("like")){

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
    }
}
