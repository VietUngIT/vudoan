package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.MarketPriceService;
import vietung.it.dev.core.services.imp.MarketPriceServiceImp;
import vietung.it.dev.core.utils.Utils;

public class MarketPriceHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        MarketPriceService service = new MarketPriceServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("getallcate")){
                String strOfset = request.getParam("ofset");
                return getAllCateMarketPriceHandler(strOfset,service);
            }else if(type.equals("getbycate")){
                String idCate = request.getParam("idcate");
                return getMarketPriceByCateHandler(idCate,service);
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getAllCateMarketPriceHandler(String strOfset, MarketPriceService service) throws Exception{
        if(strOfset!=null){
            try{
                int ofset = Integer.parseInt(strOfset);
                return service.getAllTypeMarketPrice(ofset);
            }catch (Exception e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }
        }else{
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getMarketPriceByCateHandler(String idCate, MarketPriceService service) throws Exception{
        if(idCate!=null){
            return service.getMarketPriceByCate(idCate);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
