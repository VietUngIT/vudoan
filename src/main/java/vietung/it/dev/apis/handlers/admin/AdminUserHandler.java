package vietung.it.dev.apis.handlers.admin;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.handlers.BaseApiHandler;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.SimpleResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.UserService;
import vietung.it.dev.core.services.imp.UserServiceImp;
import vietung.it.dev.core.utils.Utils;

public class AdminUserHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        UserService userService = new UserServiceImp();
        String type = request.getParam("t");
        if(type != null){
           if(type.equals("login")){
                String phone = request.getParam("ph");
                String pass = request.getParam("p");
                return loginHandler(phone,pass,userService);
            }else if(type.equals("crole")){
               String phone = request.getParam("ph");
               String pass = request.getParam("p");
               String r = request.getParam("role");
               return changeRoleHandler(phone,pass,r,userService);
           }else{
               return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse loginHandler(String phone, String pass, UserService userService) {
        if(phone!=null && pass!=null){
            return userService.loginAdmin(phone,pass);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
    private BaseResponse changeRoleHandler(String phone, String pass, String r, UserService userService) {
        if(phone!=null && pass!=null && r!=null){
            try {
                int role = Integer.parseInt(r);
                return userService.changeRole(phone,pass,role);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép dữ liệu kiểu của role.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
