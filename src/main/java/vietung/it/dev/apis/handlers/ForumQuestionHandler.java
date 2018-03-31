package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
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
                return null;
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
                String phone = request.getParam("ph");
                String like = request.getParam("like");
                String id = request.getParam("id");
                return likeQuestionHandler(like,id,phone,service);
            }else if(type.equals("getbyfield")){
                String id = request.getFormAttribute("idfield");
                String strofset = request.getParam("ofset");
                String strpage = request.getParam("page");
                String phone = request.getParam("ph");
                return getQuestionByFieldHandle(id,phone,strofset,strpage,service);
            }  else{
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
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
            Boolean isLike = Boolean.parseBoolean(like);
            try{
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

    private BaseResponse editQuestionHandle(String phone,String id, String content, ForumQuestionService service) throws Exception{
        if(phone!=null && content!=null && id!=null && !phone.trim().equals("") && !content.trim().equals("") && !id.trim().equals("")){
            return service.editQuestion(phone,id,content);
        }else{
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
