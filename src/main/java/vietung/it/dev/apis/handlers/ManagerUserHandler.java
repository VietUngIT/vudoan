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
            String oldpass = request.getParam("pass");
            String newpass = request.getParam("np");
            return changePassHandler(phone,oldpass,newpass,userService);
        }else if(type.equals("cphone")){
            String oldphone = request.getParam("ph");
            String newphone = request.getParam("nph");
            return changePhoneHandler(oldphone,newphone,userService);
        }else if(type.equals("caddress")){
            String phone = request.getParam("ph");
            String address = request.getParam("address");
            return changeAddressHandler(phone,address,userService);
        }else if(type.equals("cname")){
            String phone = request.getParam("ph");
            String name = request.getParam("name");
            return changeNameHandler(phone,name,userService);
        }else if(type.equals("get")){
            String phone = request.getParam("ph");
            return getUserInfoHandler(phone,userService);
        }else{
            SimpleResponse response = new SimpleResponse();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }
    }

    private BaseResponse changeNameHandler(String phone, String name, UserService userService) {
        if(phone!=null && name!=null ){
            return userService.changeName(phone,name);
        }else {
            SimpleResponse response = new SimpleResponse();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }
    }

    private BaseResponse changeAddressHandler(String phone, String address, UserService userService) {
        if(phone!=null && address!=null ){
            return userService.changeAddress(phone,address);
        }else {
            SimpleResponse response = new SimpleResponse();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }
    }

    private BaseResponse getUserInfoHandler(String phone, UserService userService) {
        if(phone!=null){
            return userService.getUserInfor(phone);
        }else {
            SimpleResponse response = new SimpleResponse();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }
    }

    private BaseResponse changePhoneHandler(String oldphone, String newphone, UserService userService) {
        if(oldphone!=null && newphone!=null ){
            return userService.changePhone(oldphone,newphone);
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
