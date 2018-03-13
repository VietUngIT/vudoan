package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.SimpleResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.UserService;
import vietung.it.dev.core.services.imp.UserServiceImp;

public class ManagerUserHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        UserService userService = new UserServiceImp();
        String type = request.getParam("t");
        if(type.equals("cpass")){
            String phone = request.getParam("ph");
            String oldpass = request.getParam("p");
            String newpass = request.getParam("np");
            return changePassHandler(phone,oldpass,newpass,userService);
        }else if(type.equals("cphone")){
            String oldphone = request.getParam("ph");
            String pass = request.getParam("p");
            String newphone = request.getParam("nph");
            return changePhoneHandler(oldphone,newphone,pass,userService);
        }else{
            SimpleResponse response = new SimpleResponse();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }
    }


    private BaseResponse changePhoneHandler(String oldphone, String newphone, String pass, UserService userService) {
        if(oldphone!=null && newphone!=null && pass!=null){
            return userService.changePhone(oldphone,newphone,pass);
        }else {
            SimpleResponse response = new SimpleResponse();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }
    }

    private BaseResponse changePassHandler(String phone, String oldpass, String newpass, UserService userService) {
        if(phone!=null && oldpass!=null && newpass!=null){
            return userService.changePass(phone,oldpass,newpass);
        }else {
            SimpleResponse response = new SimpleResponse();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }
    }
}
