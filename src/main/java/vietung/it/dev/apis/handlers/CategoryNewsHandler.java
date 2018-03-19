package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.CategoryService;
import vietung.it.dev.core.services.imp.CategoryServiceImp;
import vietung.it.dev.core.utils.Utils;

public class CategoryNewsHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        CategoryService service = new CategoryServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("getall")){
                return service.getAllCategoryNews();
            }else if(type.equals("get")){
                String id = request.getParam("id");
                return service.getCategoryNewsById(id);
            }else{
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid Params.");
        }
    }
}
