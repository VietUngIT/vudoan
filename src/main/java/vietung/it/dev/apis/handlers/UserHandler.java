package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.SimpleResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.UserService;
import vietung.it.dev.core.services.imp.UserServiceImp;

public class UserHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        UserService userService = new UserServiceImp();
        String type = request.getParam("t");
        if(type != null){
            if(type.equals("register")){
                String name = request.getParam("n");
                String phone = request.getParam("ph");
                String pass = request.getParam("p");
                return registerHandler(name,phone,pass,userService);
            }else if(type.equals("login")){
                String phone = request.getParam("ph");
                String pass = request.getParam("p");
                return loginHandler(phone,pass,userService);
            }else{
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


    private BaseResponse loginHandler(String phone, String pass, UserService userService) {
        if(phone!=null && pass!=null){
            return userService.login(phone,pass);
        }else {
            SimpleResponse response = new SimpleResponse();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }
    }

    private BaseResponse registerHandler(String name, String phone, String pass,UserService userService) {
        if(name!=null && phone!=null && pass!=null){
            return userService.register(name,phone,pass,0);
        }else {
            SimpleResponse response = new SimpleResponse();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }
    }
}
