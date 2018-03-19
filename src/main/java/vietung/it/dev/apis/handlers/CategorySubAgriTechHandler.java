package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.SubCategoryService;
import vietung.it.dev.core.services.imp.SubCategoryServiceImp;
import vietung.it.dev.core.utils.Utils;

public class CategorySubAgriTechHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        SubCategoryService service = new SubCategoryServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("getall")){
                return service.getAllSubCategoryAgritech();
            }else if(type.equals("getbycate")){
                String id = request.getParam("id");
                if(id!=null){
                    return service.getSubCategoryAgriTechByCate(id);
                }
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }else if(type.equals("get")){
                String id = request.getParam("id");
                if(id!=null){
                    return service.getSubCategoryAgritechById(id);
                }
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }else{
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid Params.");
        }
    }
}
