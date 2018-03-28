package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.ExpertService;
import vietung.it.dev.core.services.imp.ExpertServiceImp;
import vietung.it.dev.core.utils.Utils;

public class ExpertInfoHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        ExpertService service = new ExpertServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("online")){
                String online = request.getParam("online");
                String id = request.getParam("idexpert");
                if(online!=null && id!=null){
                    try{
                        Boolean isOnline = Boolean.parseBoolean(online);
                        return service.updateStatusOnlineExpert(isOnline,id);
                    }catch (Exception e){
                        return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu."+e.getMessage());
                    }
                }else{
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }

            }else if(type.equals("get")){
                String id = request.getParam("idexpert");
                if(id!=null){
                    return service.getInfoExpert(id);
                }else{
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            } else if(type.equals("getbydistance")){
                String strLat = request.getParam("lat");
                String strLon = request.getParam("long");
                String strNumExpert = request.getParam("nexpert");
                String fieldExpert = request.getParam("field");
                if(strLat!=null && strLon!=null && strNumExpert!=null && fieldExpert!=null){
                    try{
                        Double lat = Double.parseDouble(strLat);
                        Double lon = Double.parseDouble(strLon);
                        int numExpert = Integer.parseInt(strNumExpert);
                        return service.getListExpertNearest(lat,lon,numExpert,fieldExpert);
                    }catch (Exception e){
                        return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu."+e.getMessage());
                    }
                }else{
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            } else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }

    }
}
