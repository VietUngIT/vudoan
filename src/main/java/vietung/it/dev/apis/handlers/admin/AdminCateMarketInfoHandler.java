package vietung.it.dev.apis.handlers.admin;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.handlers.BaseApiHandler;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.CategoryService;
import vietung.it.dev.core.services.imp.CategoryServiceImp;
import vietung.it.dev.core.utils.Utils;

public class AdminCateMarketInfoHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        CategoryService service = new CategoryServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("add")){
                String strName = request.getParam("name");
                if(strName!=null){
                    return service.addCategoryMarketInfo(strName);
                }
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }else if(type.equals("edit")){
                String id = request.getParam("id");
                String strNewName = request.getParam("name");
                if(id!=null && strNewName!=null){
                    return service.editCategoryMarketInfo(id,strNewName);
                }
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }else if(type.equals("del")){
                String id = request.getParam("id");
                if(id!=null){
                    return service.deleteCategoryMarketInfo(id);
                }
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }else{
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
