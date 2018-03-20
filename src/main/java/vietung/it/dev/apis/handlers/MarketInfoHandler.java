package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.MarketInfoService;
import vietung.it.dev.core.services.imp.MarketInfoServiceImp;
import vietung.it.dev.core.utils.Utils;

public class MarketInfoHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        MarketInfoService service = new MarketInfoServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("getbycate")){
                String idcate = request.getParam("idcate");
                String ofset = request.getParam("ofset");
                String strpage = request.getParam("page");
                return getAllNewsByCateWithNewestHandler(strpage,idcate,ofset,service);
            }else if(type.equals("get")){
                String id = request.getParam("id");
                return getNewsMarketInfoByIdHandler(id,service);
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getNewsMarketInfoByIdHandler(String id, MarketInfoService service) {
        if(id!=null){
            try{
                return service.getNewsMarketInfoById(id);
            }catch (Exception e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.SYSTEM_ERROR,"Lỗi hệ thống."+e.getMessage());
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getAllNewsByCateWithNewestHandler(String strpage, String idcate, String ofset, MarketInfoService service) {
        if(ofset!=null && idcate!=null){
            if(strpage==null){
                strpage="0";
            }
            try {
                int page = Integer.parseInt(strpage);
                int ofs = Integer.parseInt(ofset);
                return service.getAllNewsMarketInfoByCate(page,ofs,idcate);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
