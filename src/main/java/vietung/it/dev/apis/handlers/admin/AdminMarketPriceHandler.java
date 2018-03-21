package vietung.it.dev.apis.handlers.admin;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.handlers.BaseApiHandler;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.MarketPriceService;
import vietung.it.dev.core.services.imp.MarketPriceServiceImp;
import vietung.it.dev.core.utils.Utils;

public class AdminMarketPriceHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        MarketPriceService service = new MarketPriceServiceImp();
        String type = request.getFormAttribute("t");
        if(type!=null){
            if(type.equals("add")){
                String idCate = request.getFormAttribute("idcate");
                String name = request.getFormAttribute("name");
                String strprice = request.getFormAttribute("price");
                String place = request.getFormAttribute("place");
                String unit = request.getFormAttribute("unit");
                String note = request.getFormAttribute("note");
                return createMarketPriceHandler(idCate,name,strprice,place,unit,note,service);
            }if(type.equals("edit")){
                String id = request.getFormAttribute("id");
                String idCate = request.getFormAttribute("idcate");
                String name = request.getFormAttribute("name");
                String strprice = request.getFormAttribute("price");
                String place = request.getFormAttribute("place");
                String unit = request.getFormAttribute("unit");
                String note = request.getFormAttribute("note");
                return editMarketPriceHandler(id,idCate,name,strprice,place,unit,note,service);
            }else if(type.equals("del")){
                String id = request.getFormAttribute("id");
                if(id!=null){
                    return service.deleteMarketPrice(id);
                }else {
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
}

    private BaseResponse editMarketPriceHandler(String id, String idCate, String name, String strprice, String place, String unit, String note, MarketPriceService service) throws Exception{
        if(id!=null){
            return service.editMarketPrice(id,idCate,name,strprice,place,unit,note);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse createMarketPriceHandler(String idCate, String name, String strprice, String place, String unit, String note, MarketPriceService service) throws Exception{
        if(idCate!=null && name!=null && strprice!=null && place!=null && unit!=null){
            try {
                long price = Long.parseLong(strprice);
                return service.createMarketPrice(idCate,name,price,place,unit,note);
            }catch (Exception e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
