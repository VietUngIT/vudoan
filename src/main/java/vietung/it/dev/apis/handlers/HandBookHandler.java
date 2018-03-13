package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.HandbookService;
import vietung.it.dev.core.services.imp.HandbookServiceImp;
import vietung.it.dev.core.utils.Utils;

public class HandBookHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        HandbookService service = new HandbookServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("getall")){
                String timelast = request.getParam("timelast");
                String offer = request.getParam("offer");
                return getAllHandBookHandler(timelast,offer,service);
            }if(type.equals("getbytype")){
                String timelast = request.getParam("timelast");
                String idtype = request.getParam("idtype");
                String offer = request.getParam("offer");
                return getAllandBookByTypeHandler(timelast,idtype,offer,service);
            }else if(type.equals("get")){
                String idHB = request.getParam("idn");
                return getHandBookByIdHandler(idHB,service);
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getHandBookByIdHandler(String idHB, HandbookService service) {
        if(idHB!=null){
            try{
                return service.getHandbookById(idHB);
            }catch (Exception e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.SYSTEM_ERROR,"error: "+e.getMessage());
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getAllandBookByTypeHandler(String timelast, String idtype, String offer, HandbookService service) {
        if(timelast!=null && offer!=null && idtype!=null){
            try {
                long tlast = Long.parseLong(timelast);
                int of = Integer.parseInt(offer);
                return service.getAllHandbookByType(tlast,of,idtype);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getAllHandBookHandler(String timelast, String offer, HandbookService service) {
        if(timelast!=null && offer!=null){
            try {
                long tlast = Long.parseLong(timelast);
                int of = Integer.parseInt(offer);
                return service.getAllHandbook(tlast,of);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
