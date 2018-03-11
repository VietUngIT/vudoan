package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.PriceMarketService;
import vietung.it.dev.core.services.imp.PriceMarketServiceImp;
import vietung.it.dev.core.utils.Utils;

public class PriceMarketHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        PriceMarketService service = new PriceMarketServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("ad")){
                String namePlace = request.getParam("place");
                String pr = request.getParam("price");
                String typeNews = request.getParam("typenews");
                String nameType = request.getParam("nametype");
                if(namePlace!=null && pr!=null && typeNews!=null){
                    try{
                        long price = Long.parseLong(pr);
                        return service.addPriceMarket(namePlace,price,typeNews,nameType);
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                        return  Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu.");
                    }
                }else {
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            }else if(type.equals("ed")){
                String idPM = request.getParam("idpm");
                String namePlace = request.getParam("place");
                String pr = request.getParam("price");
                if(namePlace!=null && pr!=null && idPM!=null ){
                    try{
                        long price = Long.parseLong(pr);
                        return service.editPriceMarket(idPM,namePlace,price);
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                        return  Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu.");
                    }
                }else {
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            }else if(type.equals("del")){
                String idPM = request.getParam("idpm");
                if(idPM!=null){
                    return service.deletePriceMarket(idPM);
                }else {
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            }else if(type.equals("getall")){
                String lastId = request.getParam("lastId");
                String ofs = request.getParam("ofset");
                try {
                    if(lastId==null) lastId="";
                    if(ofs==null) ofs="10";
                    int ofset = Integer.parseInt(ofs);
                    return service.getAllPriceMarket(lastId,ofset);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    return  Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu.");
                }

            }else if(type.equals("idtype")){
                String idType = request.getParam("id");
                String lastId = request.getParam("lastId");
                String ofs = request.getParam("ofset");
                try {
                    if(lastId==null) lastId="";
                    if(ofs==null) ofs="10";
                    int ofset = Integer.parseInt(ofs);
                    return service.getPriceMarketByType(idType,lastId,ofset);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    return  Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu.");
                }
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
