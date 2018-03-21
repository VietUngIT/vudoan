package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.consts.Variable;
import vietung.it.dev.core.services.CommentService;
import vietung.it.dev.core.services.imp.CommentServiceImp;
import vietung.it.dev.core.utils.Utils;

public class CommentHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        CommentService service = new CommentServiceImp();
        String type = request.getFormAttribute("t");
        if(type!=null){
            if(type.equals("cmt")){
                String idNews = request.getFormAttribute("idn");
                String phone = request.getFormAttribute("ph");
                String content = request.getFormAttribute("content");
                String tCmt = request.getFormAttribute("typecmt");
                return commentNewsHandler(idNews,tCmt,phone,content,service);
            }else if(type.equals("delcmt")){
                String idCmnt = request.getFormAttribute("idcmnt");
                String phone = request.getFormAttribute("ph");
                String tCmt = request.getFormAttribute("typecmt");
                return deleteCommentNewsHandler(idCmnt,phone,tCmt,service);
            }else if(type.equals("editcmt")){
                String idCmnt = request.getFormAttribute("idcmnt");
                String phone = request.getFormAttribute("ph");
                String content = request.getFormAttribute("content");
                String tCmt = request.getFormAttribute("typecmt");
                return editCommentNewsHandler(idCmnt,phone,content,tCmt,service);
            }else if(type.equals("getbynews")){
                String idNews = request.getFormAttribute("idnews");
                String offset = request.getFormAttribute("off");
                String strPage = request.getFormAttribute("page");
                String tCmt = request.getFormAttribute("typecmt");
                return getCommentbyNewsHandler(idNews,offset,strPage,tCmt,service);
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getCommentbyNewsHandler(String idNews, String offset, String strPage, String tCmt, CommentService service) throws Exception{
        if(idNews!=null && offset!=null && tCmt!=null){
            if(strPage==null){
                strPage="0";
            }
            try{
                int ofs = Integer.parseInt(offset);
                int page = Integer.parseInt(strPage);
                int type = Integer.parseInt(tCmt);
                return service.getCommentbyNews(idNews,ofs,page,type);
//                if(type == Variable.COMMENTS_NEWS){
//                }else if(type == Variable.COMMENTS_AGRI_TECH){
//                    return service.getCommentbyNews(idNews,ofs,page,Variable.COMMENTS_AGRI_TECH);
//                }else if(type == Variable.COMMENTS_MARKET_INFO){
//                    return service.getCommentbyNews(idNews,ofs,page,Variable.COMMENTS_MARKET_INFO);
//                }else {
//                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
//                }
            }catch (Exception e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.SYSTEM_ERROR,e.getMessage());
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse editCommentNewsHandler(String idCmnt, String phone, String content, String tCmt, CommentService service) throws Exception {
        if(idCmnt!=null && phone!=null && content!=null && tCmt!=null){
            try{
                int type = Integer.parseInt(tCmt);
                return service.editCommentNews(idCmnt,phone,content,type);
//                if(type == Variable.COMMENTS_NEWS){
//                }else if(type == Variable.COMMENTS_AGRI_TECH){
//                    return service.editCommentNews(idCmnt,phone,content,Variable.MG_COMMENTS_AGRI_TECH);
//                }else if(type == Variable.COMMENTS_MARKET_INFO){
//                    return service.editCommentNews(idCmnt,phone,content,Variable.MG_COMMENTS_MARKET_INFO);
//                }else {
//                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
//                }
            }catch (Exception e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.SYSTEM_ERROR,e.getMessage());
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse deleteCommentNewsHandler(String idCmnt, String phone, String tCmt, CommentService service) throws Exception{
        if(idCmnt!=null && phone!=null && tCmt!=null){
            try{
                int type = Integer.parseInt(tCmt);
                return service.deleteCommentNews(idCmnt,phone,type);
//                if(type == Variable.COMMENTS_NEWS){
//                }else if(type == Variable.COMMENTS_AGRI_TECH){
//                    return service.deleteCommentNews(idCmnt,phone,Variable.MG_COMMENTS_AGRI_TECH);
//                }else if(type == Variable.COMMENTS_MARKET_INFO){
//                    return service.deleteCommentNews(idCmnt,phone,Variable.MG_COMMENTS_MARKET_INFO);
//                }else {
//                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
//                }
            }catch (Exception e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.SYSTEM_ERROR,e.getMessage());
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse commentNewsHandler(String idNews, String tCmt, String phone, String content, CommentService service) throws Exception{
        if(idNews!=null && phone!=null && content!=null && tCmt!=null){
            try{
                int type = Integer.parseInt(tCmt);
                return service.commentNews(idNews,phone,content,type);
//                if(type == Variable.COMMENTS_NEWS){
//                }else if(type == Variable.COMMENTS_AGRI_TECH){
//                    return service.commentNews(idNews,phone,content,Variable.MG_COMMENTS_AGRI_TECH);
//                }else if(type == Variable.COMMENTS_MARKET_INFO){
//                    return service.commentNews(idNews,phone,content,Variable.MG_COMMENTS_MARKET_INFO);
//                }else {
//                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
//                }
            }catch (Exception e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.SYSTEM_ERROR,e.getMessage());
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
