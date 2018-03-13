package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.SimpleResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.CommentNewsService;
import vietung.it.dev.core.services.imp.CommentNewsServiceImp;

public class CommentNewsNewsHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        CommentNewsService service = new CommentNewsServiceImp();
        String type = request.getFormAttribute("t");
        if(type!=null){
            if(type.equals("cmt")){
                String idNews = request.getFormAttribute("idn");
                String phone = request.getFormAttribute("ph");
                String content = request.getFormAttribute("content");
                return commentNewsHandler(idNews,phone,content,service);
            }else if(type.equals("delcmt")){
                String idCmnt = request.getFormAttribute("idcmnt");
                String phone = request.getFormAttribute("ph");
                return deleteCommentNewsHandler(idCmnt,phone,service);
            }else if(type.equals("getbynews")){
                String idNews = request.getFormAttribute("idnews");
                String offset = request.getFormAttribute("off");
                String lastCmt = request.getFormAttribute("lastCmt");
                return getCommentbyNewsHandler(idNews,offset,lastCmt,service);
            }else {
                return invalidParams();
            }
        }else {
            return invalidParams();
        }
    }

    private BaseResponse getCommentbyNewsHandler(String idNews, String offset, String lastCmt, CommentNewsService service) {
        if(idNews!=null && offset!=null && lastCmt!=null){
            try{
                int ofs = Integer.parseInt(offset);
                long last = Long.parseLong(lastCmt);
                return service.getCommentbyNews(idNews,ofs,last);
            }catch (Exception e){
                e.printStackTrace();
                return systemError(e);
            }
        }else {
            return invalidParams();
        }
    }

    private BaseResponse deleteCommentNewsHandler(String idCmnt, String phone, CommentNewsService service) {
        if(idCmnt!=null && phone!=null){
            try{
                return service.deleteCommentNews(idCmnt,phone);
            }catch (Exception e){
                e.printStackTrace();
                return systemError(e);
            }
        }else {
            return invalidParams();
        }
    }

    private BaseResponse commentNewsHandler(String idNews, String phone, String content, CommentNewsService service) {
        if(idNews!=null && phone!=null && content!=null){
            try{
                return service.commentNews(idNews,phone,content);
            }catch (Exception e){
                e.printStackTrace();
                return systemError(e);
            }
        }else {
            return invalidParams();
        }
    }

    private BaseResponse invalidParams(){
        SimpleResponse response = new SimpleResponse();
        response.setError(ErrorCode.INVALID_PARAMS);
        response.setMsg("Invalid params.");
        return response;
    }
    private BaseResponse systemError(Exception e){
        SimpleResponse response = new SimpleResponse();
        response.setError(ErrorCode.SYSTEM_ERROR);
        response.setMsg("Lỗi hệ thống."+e.getMessage());
        return response;
    }

}
