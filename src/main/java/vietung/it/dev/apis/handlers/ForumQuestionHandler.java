package vietung.it.dev.apis.handlers;

import com.google.gson.JsonArray;
import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.ForumQuestionResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.ForumQuestionService;
import vietung.it.dev.core.services.imp.ForumQuestionServiceImp;
import vietung.it.dev.core.utils.Utils;

public class ForumQuestionHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        ForumQuestionService service = new ForumQuestionServiceImp();
        String type = request.getFormAttribute("t");
        if(type!=null){
            if(type.equals("ad")){
                String phone = request.getFormAttribute("ph");
                String image = request.getFormAttribute("image");
                String content = request.getFormAttribute("content");
                String idField = request.getFormAttribute("field");
                String strNumExpert = request.getFormAttribute("nexpert");
                return addQuestionHandle(phone,image,idField,content,strNumExpert,service);
            }else if(type.equals("edit")){
                String id = request.getFormAttribute("id");
                String phone = request.getFormAttribute("ph");
                String content = request.getFormAttribute("content");
                return editQuestionHandle(phone,id,content,service);
            } else if(type.equals("del")){
                String phone = request.getFormAttribute("ph");
                String id = request.getFormAttribute("id");
                return delQuestionHandle(phone,id,service);
            }else if(type.equals("like")){
                String phone = request.getFormAttribute("ph");
                String like = request.getFormAttribute("like");
                String id = request.getFormAttribute("id");
                return likeQuestionHandler(like,id,phone,service);
            }else if(type.equals("getbyfield")){
                String id = request.getFormAttribute("idfield");
                String strofset = request.getFormAttribute("ofset");
                String strpage = request.getFormAttribute("page");
                String phone = request.getFormAttribute("ph");
                return getQuestionByFieldHandle(id,phone,strofset,strpage,service);
            }else if(type.equals("getbyiduser")){
                String id = request.getFormAttribute("iduser");
                String strofset = request.getFormAttribute("ofset");
                String strpage = request.getFormAttribute("page");
                String phone = request.getFormAttribute("phone");
                return getQuestionByIDUserHandle(id,strofset,strpage,phone,service);
            }else if(type.equals("getbyidexpert")){
                String id = request.getFormAttribute("idexpert");
                String strofset = request.getFormAttribute("ofset");
                String strpage = request.getFormAttribute("page");
                return getQuestionByIDExpertHandle(id,strofset,strpage,service);
            }else if(type.equals("getbyid")){
                String id = request.getFormAttribute("id");
                String phone = request.getFormAttribute("ph");
                return getQuestionByIDHandle(id,phone,service);
            }else if(type.equals("getexpertbyidquestion")){
                String id = request.getFormAttribute("idquestion");
                String strLat = request.getFormAttribute("lat");
                String strLon = request.getFormAttribute("long");
                String strstatus = request.getFormAttribute("status");
                return getExpertByIDQuestionHandle(id,strLat,strLon,strstatus,service);
            }else if(type.equals("getall")){
                String strofset = request.getFormAttribute("ofset");
                String strpage = request.getFormAttribute("page");
                String phone = request.getFormAttribute("ph");
                return getQuestionAllHandle(phone,strofset,strpage,service);
            }else{
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getQuestionByIDExpertHandle(String id, String strofset, String strpage, ForumQuestionService service) throws Exception {
        if(id!=null && strofset!=null && strpage!=null){
            try {
                int ofs = Integer.parseInt(strofset);
                int page = Integer.parseInt(strpage);
                return service.getExpertByIDExpertQuestion(id,ofs,page);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getQuestionByIDUserHandle(String id, String strofset, String strpage,String phone, ForumQuestionService service) throws Exception {
        if(id!=null && strofset!=null && strpage!=null && phone!=null){
            try {
                int ofs = Integer.parseInt(strofset);
                int page = Integer.parseInt(strpage);
                return service.getExpertByIDUserQuestion(id,ofs,page,phone);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getExpertByIDQuestionHandle(String id,String strLat,String strLon,String strstatus,ForumQuestionService service) throws Exception {
        if(id!=null){
            if(strstatus==null || strstatus.equals("")){
                strstatus = "-1";
            }
            try {
                Double lat = Double.parseDouble(strLat);
                Double lon = Double.parseDouble(strLon);
                int status = Integer.parseInt(strstatus);
                return service.getExpertByIDQuestion(id,lat,lon,status);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse addQuestionHandle(String phone, String image,String idField, String content, String strNExpert, ForumQuestionService service) throws Exception {

        if(phone!=null && content!=null && idField!=null ){
            if(strNExpert==null){
                strNExpert = "5";
            }
            try {
                int nExpert = Integer.parseInt(strNExpert);
                return service.addQuestion(phone,image,idField,content,nExpert);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }
        }else{
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getQuestionByIDHandle(String id, String phone, ForumQuestionService service) throws Exception {
        if(id!=null && phone!=null && !id.trim().equals("") && !phone.trim().equals("")){
            return service.getQuestionByID(id,phone);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getQuestionByFieldHandle(String id, String phone, String strofset, String strpage, ForumQuestionService service) throws Exception {
        if(strofset!=null && id!=null){
            if(strpage==null){
                strpage="0";
            }
            try {
                int page = Integer.parseInt(strpage);
                int ofset = Integer.parseInt(strofset);
                return service.getQuestionByField(page,ofset,id,phone);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse likeQuestionHandler(String like, String id, String phone, ForumQuestionService service) throws Exception {
        if(like!=null && id!=null && !like.trim().equals("") && !id.trim().equals("")){
            try{
                Boolean isLike = Boolean.parseBoolean(like);
                return service.likeQuestion(id,isLike,phone);
            }catch (Exception e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.SYSTEM_ERROR,"Lỗi hệ thống."+e.getMessage());
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse delQuestionHandle(String phone,String id, ForumQuestionService service) throws Exception {
        if(id!=null && phone!=null && !phone.trim().equals("") && !id.trim().equals("")){
            return service.delQuestion(phone,id);
        }else{
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse editQuestionHandle(String phone,String id, String content,ForumQuestionService service) throws Exception{
        if(phone!=null && content!=null && id!=null && !phone.trim().equals("") && !content.trim().equals("") && !id.trim().equals("")){
            return service.editQuestion(phone,id,content);
        }else{
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getQuestionAllHandle(String phone, String strofset, String strpage, ForumQuestionService service) throws Exception {
        if(strofset!=null){
            if(strpage==null){
                strpage="0";
            }
            try {
                int page = Integer.parseInt(strpage);
                int ofset = Integer.parseInt(strofset);
                return service.getQuestionAll(page,ofset,phone);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
