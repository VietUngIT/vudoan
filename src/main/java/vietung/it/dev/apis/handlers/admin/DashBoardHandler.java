package vietung.it.dev.apis.handlers.admin;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.handlers.BaseApiHandler;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.DashBoardService;
import vietung.it.dev.core.services.imp.DashBoardServiceImp;
import vietung.it.dev.core.utils.Utils;

public class DashBoardHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        DashBoardService service = new DashBoardServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("getdashboard")){
                return service.getDashBoardByCurrentDay();
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
