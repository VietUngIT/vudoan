package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.UploadService;
import vietung.it.dev.core.services.imp.UploadServiceImp;
import vietung.it.dev.core.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;

public class UploadHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        UploadService service = new UploadServiceImp();
        String type = request.getFormAttribute("t");
        if(type.equals("cavatar")){
            String phone = request.getFormAttribute("ph");
            String avatar = request.getFormAttribute("avatar");
            return changeAvatarHandler(phone,avatar,service);
        }else{
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse changeAvatarHandler(String phone, String avatar, UploadService service) {
        if(phone!=null && avatar!=null ){
            return service.changeAvatar(phone,avatar);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

}
