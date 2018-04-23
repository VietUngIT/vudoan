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
        String type = request.getFormAttribute("t");
        if(type!=null){
            if(type.equals("add")){
                String strName = request.getFormAttribute("name");
                String strImage = request.getFormAttribute("image");
                if(strName!=null){
                    return service.addCategoryMarketPrice(strName,strImage);
                }
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }else if(type.equals("editname")){
                String id = request.getFormAttribute("id");
                String strNewName = request.getFormAttribute("name");
                if(id!=null && strNewName!=null){
                    return service.editNameCategoryMarketPrice(id,strNewName);
                }
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }else if(type.equals("editimage")){
                String id = request.getFormAttribute("id");
                String strNewImage = request.getFormAttribute("image");
                if(id!=null && strNewImage!=null){
                    return service.editImageCategoryMarketPrice(id,strNewImage);
                }
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }else if(type.equals("del")){
                String id = request.getFormAttribute("id");
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
