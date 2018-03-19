package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.CategoryMarketPriceService;
import vietung.it.dev.core.services.imp.CategoryMarketPriceServiceImp;
import vietung.it.dev.core.utils.Utils;

public class CategoryMarketPriceHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        CategoryMarketPriceService service = new CategoryMarketPriceServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("getall")){
                return service.getAllCategoryMarketPrice();
            }else if(type.equals("get")){
                String id = request.getParam("id");
                if(id!=null){
                    return service.getCategoryMarketPriceById(id);
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
