package vietung.it.dev.apis.handlers.admin;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.handlers.BaseApiHandler;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.ExpertService;
import vietung.it.dev.core.services.imp.ExpertServiceImp;
import vietung.it.dev.core.utils.Utils;

public class AdminExpertHandler extends BaseApiHandler{
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        ExpertService service = new ExpertServiceImp();
        String type = request.getFormAttribute("t");
        if(type!=null){
            if(type.equals("ad")){
                String name = request.getFormAttribute("name");
                String phone = request.getFormAttribute("phone");
                String desc = request.getFormAttribute("desc");
                String email = request.getFormAttribute("email");
                String address = request.getFormAttribute("address");
                String workplace = request.getFormAttribute("workplace");
                String idParentField = request.getFormAttribute("idparentfield");
//                String strlat = request.getFormAttribute("lat");
//                String strlon = request.getFormAttribute("long");
                String field = request.getFormAttribute("field");
                String tags = request.getFormAttribute("tags");
                String degree = request.getFormAttribute("degree");
                return addExpertHandle(name,phone,desc,email,address,idParentField,field,tags,degree,workplace,service);
            }else if(type.equals("edit")){
                String phone = request.getFormAttribute("phone");
                String desc = request.getFormAttribute("desc");
                String workplace = request.getFormAttribute("workplace");
                String strlat = request.getFormAttribute("lat");
                String strlon = request.getFormAttribute("long");
                String idParentField = request.getFormAttribute("idparentfield");
                String email = request.getFormAttribute("email");
                return editExpertHandle(phone,desc,strlat,strlon,email,idParentField,workplace,service);
            }else if(type.equals("editdegree")){
                String phone = request.getFormAttribute("phone");
                String degree = request.getFormAttribute("degree");
                return editDegreeExpertHandle(phone,degree,service);
            } else if(type.equals("editfield")){
                String phone = request.getFormAttribute("phone");
                String idfield = request.getFormAttribute("idfield");
                return editFieldExpertHandle(phone,idfield,service);
            } else if(type.equals("del")){
                String phone = request.getFormAttribute("phone");
                return delExpertHandle(phone,service);
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse editFieldExpertHandle(String phone, String idfield, ExpertService service) throws Exception {
        if(phone!=null){
            if(idfield==null){
                idfield="[]";
            }
            return service.editFieldExpert(phone,idfield);
        }else{
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse editDegreeExpertHandle(String phone, String degree, ExpertService service) throws Exception {
        if(phone!=null){
            if(degree==null){
                degree="[]";
            }
            return service.editDegreeExpert(phone,degree);
        }else{
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse editExpertHandle(String phone, String desc, String strLat, String strLon, String email,String idParentField,String workplace, ExpertService service) throws Exception {
        if(phone!=null && idParentField!=null){
            try{
                Double lat = Double.parseDouble(strLat);
                Double lon = Double.parseDouble(strLon);
                return service.editExpert(phone,desc,lat,lon,email,idParentField,workplace);
            }catch (Exception e){
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu."+e.getMessage());
            }
        }else{
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse delExpertHandle(String phone, ExpertService service) throws Exception {
        if(phone!=null){
            return service.deleteExpert(phone);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }


    private BaseResponse addExpertHandle(String name, String phone, String desc, String email, String address,String idParentField, String field, String tags, String degree,String workplace, ExpertService service) throws Exception {
        if(name!=null && phone!=null && email!=null && address!=null && idParentField!=null && workplace!=null){
            if(field==null){
                field="[]";
            }
            if(tags==null){
                tags="[]";
            }
            if(degree==null){
                degree="[]";
            }
            return service.addExpert(name,phone,desc,email,address,idParentField,field,tags,degree,workplace);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
