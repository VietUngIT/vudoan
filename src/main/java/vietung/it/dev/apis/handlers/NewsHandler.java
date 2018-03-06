package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.SimpleResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.NewsService;
import vietung.it.dev.core.services.imp.NewsServiceImp;

public class NewsHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        NewsService service = new NewsServiceImp();
        String type = request.getFormAttribute("t");
        if(type!=null){
            if(type.equals("getall")){
                return null;
            }else if(type.equals("get")){
                return null;
            }else if(type.equals("cr")){
                return null;
            }else if(type.equals("ed")){
                return null;
            }else if(type.equals("del")){
                String idNews = request.getFormAttribute("idn");
                String phone = request.getFormAttribute("phone");
                if(idNews!=null){
                    return service.deleteNews(idNews,phone);
                }else {
                    SimpleResponse response = new SimpleResponse();
                    response.setError(ErrorCode.INVALID_PARAMS);
                    response.setMsg("Invalid params.");
                    return response;
                }
            }else if(type.equals("comment")){
                String idNews = request.getFormAttribute("idn");
                String phone = request.getFormAttribute("phone");
                String content = request.getFormAttribute("content");
                return commentNewsHandler(idNews,phone,content,service);
            }else if(type.equals("like")){
                String like = request.getFormAttribute("like");
                String idNews = request.getFormAttribute("idn");
                return likeNewsHandler(like,idNews,service);
            }else if(type.equals("view")){
                String idNews = request.getFormAttribute("idn");
                return viewNewsHandler(idNews,service);
            }else {
                SimpleResponse response = new SimpleResponse();
                response.setError(ErrorCode.INVALID_PARAMS);
                response.setMsg("Invalid params.");
                return response;
            }
        }else {
            SimpleResponse response = new SimpleResponse();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }
    }

    private BaseResponse commentNewsHandler(String idNews, String phone, String content, NewsService service) {
        if(idNews!=null && phone!=null && content!=null){
            try{
                return service.commentNews(idNews,phone,content);
            }catch (Exception e){
                e.printStackTrace();
                SimpleResponse response = new SimpleResponse();
                response.setError(ErrorCode.SYSTEM_ERROR);
                response.setMsg("Lỗi hệ thống."+e.getMessage());
                return response;
            }
        }else {
            SimpleResponse response = new SimpleResponse();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }
    }

    private BaseResponse viewNewsHandler(String idNews, NewsService service) {
        if(idNews!=null){
            try{
                return service.viewNews(idNews);
            }catch (Exception e){
                e.printStackTrace();
                SimpleResponse response = new SimpleResponse();
                response.setError(ErrorCode.SYSTEM_ERROR);
                response.setMsg("Lỗi hệ thống."+e.getMessage());
                return response;
            }

        }else {
            SimpleResponse response = new SimpleResponse();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }
    }

    private BaseResponse likeNewsHandler(String like, String idNews,NewsService service){
        if(like!=null && idNews!=null){
            Boolean isLike = Boolean.parseBoolean(like);
            try{
                return service.likeNews(idNews,isLike);
            }catch (Exception e){
                e.printStackTrace();
                SimpleResponse response = new SimpleResponse();
                response.setError(ErrorCode.SYSTEM_ERROR);
                response.setMsg("Lỗi hệ thống."+e.getMessage());
                return response;
            }

        }else {
            SimpleResponse response = new SimpleResponse();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }
    }
}
