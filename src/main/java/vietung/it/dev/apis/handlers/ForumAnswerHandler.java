package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.ForumAnswerService;
import vietung.it.dev.core.services.imp.ForumAnswerServiceImp;
import vietung.it.dev.core.utils.Utils;

public class ForumAnswerHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        ForumAnswerService service = new ForumAnswerServiceImp();
        String type = request.getFormAttribute("t");
        if(type!=null){
            if(type.equals("ad")){
                String id = request.getFormAttribute("idquestion");
                String phone = request.getFormAttribute("ph");
                String content = request.getFormAttribute("content");
                return addAnswerHandle(id,phone,content,service);
            }else if(type.equals("edit")){
                String id = request.getFormAttribute("id");
                String phone = request.getFormAttribute("ph");
                String content = request.getFormAttribute("content");
                return editAnswerHandle(phone,id,content,service);
            } else if(type.equals("del")){
                String phone = request.getFormAttribute("ph");
                String id = request.getFormAttribute("id");
                return delAnswerHandle(phone,id,service);
            }else if(type.equals("like")){
                String phone = request.getParam("ph");
                String like = request.getParam("like");
                String id = request.getParam("id");
                return likeAnswerHandler(like,id,phone,service);
            }else if(type.equals("getbyquestion")){
                String id = request.getFormAttribute("idquestion");
                String strofset = request.getParam("ofset");
                String strpage = request.getParam("page");
                String phone = request.getParam("ph");
                return getAnswerByQuestionHandle(id,phone,strofset,strpage,service);
            }  else{
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse addAnswerHandle(String id,String phone, String content, ForumAnswerService service) throws Exception {
        if(phone!=null && content!=null && id!=null && !id.trim().equals("") && !phone.trim().equals("") && !content.trim().equals("")){
            return service.addAnswer(id,phone,content);
        }else{
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getAnswerByQuestionHandle(String id, String phone, String strofset, String strpage, ForumAnswerService service) throws  Exception{
        if(strofset!=null && id!=null){
            if(strpage==null){
                strpage="0";
            }
            try {
                int page = Integer.parseInt(strpage);
                int ofset = Integer.parseInt(strofset);
                return service.getAnswerByQuestion(page,ofset,id,phone);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse likeAnswerHandler(String like, String id, String phone, ForumAnswerService service) throws Exception{
        if(like!=null && id!=null && !like.trim().equals("") && !id.trim().equals("")){
            Boolean isLike = Boolean.parseBoolean(like);
            try{
                return service.likeAnswer(id,isLike,phone);
            }catch (Exception e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.SYSTEM_ERROR,"Lỗi hệ thống."+e.getMessage());
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse delAnswerHandle(String phone, String id, ForumAnswerService service) throws Exception {
        if(id!=null && phone!=null && !phone.trim().equals("") && !id.trim().equals("")){
            return service.delAnswer(phone,id);
        }else{
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse editAnswerHandle(String phone, String id, String content, ForumAnswerService service) throws Exception{
        if(phone!=null && content!=null && id!=null && !phone.trim().equals("") && !content.trim().equals("") && !id.trim().equals("")){
            return service.editAnswer(phone,id,content);
        }else{
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
