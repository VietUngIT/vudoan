package vietung.it.dev.apis.handlers.admin;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.handlers.BaseApiHandler;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.CategoryMarketPriceService;
import vietung.it.dev.core.services.imp.CategoryMarketPriceServiceImp;
import vietung.it.dev.core.utils.Utils;

public class AdminCateMarketPriceHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        CategoryMarketPriceService service = new CategoryMarketPriceServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("add")){
                String strName = request.getParam("name");
                String strImage = request.getParam("image");
                if(strName!=null && strImage!=null){
                    return service.addCategoryMarketPrice(strName,strImage);
                }
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }else if(type.equals("editname")){
                String id = request.getParam("id");
                String strNewName = request.getParam("name");
                if(id!=null && strNewName!=null){
                    return service.editNameCategoryMarketPrice(id,strNewName);
                }
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }else if(type.equals("editimage")){
                String id = request.getParam("id");
                String strNewImage = request.getParam("image");
                if(id!=null && strNewImage!=null){
                    return service.editImageCategoryMarketPrice(id,strNewImage);
                }
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }else if(type.equals("del")){
                String id = request.getParam("id");
                if(id!=null){
                    return service.deleteCategoryMarketPrice(id);
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
