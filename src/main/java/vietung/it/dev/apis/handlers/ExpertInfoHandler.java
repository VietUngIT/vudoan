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

            }else if(type.equals("getall")){
                String ofset = request.getParam("ofset");
                String strpage = request.getParam("page");
                if(ofset!=null){
                    if(strpage==null || strpage==""){
                        strpage="0";
                    }
                    try{
                        int ofs = Integer.parseInt(ofset);
                        int page = Integer.parseInt(strpage);
                        return service.getAllExpert(ofs,page);
                    }catch (NumberFormatException e){
                        e.printStackTrace();
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
                String idparentfieldExpert = request.getParam("idparentfield");
                if(strLat!=null && strLon!=null && strNumExpert!=null && idparentfieldExpert!=null){
                    try{
                        Double lat = Double.parseDouble(strLat);
                        Double lon = Double.parseDouble(strLon);
                        int numExpert = Integer.parseInt(strNumExpert);
                        return service.getListExpertNearest(lat,lon,numExpert,idparentfieldExpert);
                    }catch (Exception e){
                        return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu."+e.getMessage());
                    }
                }else{
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            } else if(type.equals("edittags")){
                String phone = request.getParam("phone");
                String tags = request.getParam("tags");
                return editTagsExpertHandle(phone,tags,service);
            }else if(type.equals("rate")){
                String phone = request.getParam("phone");
                String strrate = request.getParam("rate");
                return rateExpertHandle(phone,strrate,service);
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }

    }

    private BaseResponse rateExpertHandle(String id, String strrate, ExpertService service) throws Exception {
        if(id!=null && strrate!=null){
            try{
                int rate = Integer.parseInt(strrate);
                return service.rateExpert(id,rate);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse editTagsExpertHandle(String phone, String tags, ExpertService service) throws Exception {
        if(phone!=null){
            if(tags==null){
                tags="[]";
            }
            return service.editTagsExpert(phone,tags);
        }else{
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
