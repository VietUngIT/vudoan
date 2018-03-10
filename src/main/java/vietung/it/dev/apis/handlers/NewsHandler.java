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
                String timelast = request.getParam("timelast");
                String offer = request.getParam("offer");
                return getAllNewsHandler(timelast,offer,service);
            }if(type.equals("getbytype")){
                String timelast = request.getParam("timelast");
                String idtype = request.getParam("offer");
                String offer = request.getParam("offer");
                return getAllNewsByTypeHandler(timelast,idtype,offer,service);
            }else if(type.equals("get")){
                String idNews = request.getParam("idn");
                return getNewsByIdHandler(idNews,service);
            }else if(type.equals("like")){
                String like = request.getParam("like");
                String idNews = request.getParam("idn");
                return likeNewsHandler(like,idNews,service);
            }else if(type.equals("view")){
                String idNews = request.getParam("idn");
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

    private BaseResponse getAllNewsByTypeHandler(String timelast, String idtype, String offer, NewsService service) {
        if(timelast!=null && offer!=null && idtype!=null){
            try {
                long tlast = Long.parseLong(timelast);
                int idT = Integer.parseInt(idtype);
                int of = Integer.parseInt(offer);
                return service.getAllNewsByType(tlast,of,idT);
            }catch (NumberFormatException e){
                e.printStackTrace();
                SimpleResponse response = new SimpleResponse();
                response.setError(ErrorCode.CANT_CAST_TYPE);
                response.setMsg("Lỗi ép kiểu dữ liệu.");
                return response;
            }

        }else {
            SimpleResponse response = new SimpleResponse();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }
    }

    private BaseResponse getAllNewsHandler(String timelast, String offer, NewsService service) {
        if(timelast!=null && offer!=null){
            try {
                long tlast = Long.parseLong(timelast);
                int of = Integer.parseInt(offer);
                return service.getAllNews(tlast,of);
            }catch (NumberFormatException e){
                e.printStackTrace();
                SimpleResponse response = new SimpleResponse();
                response.setError(ErrorCode.CANT_CAST_TYPE);
                response.setMsg("Lỗi ép kiểu dữ liệu.");
                return response;
            }

        }else {
            SimpleResponse response = new SimpleResponse();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }
    }

    private BaseResponse getNewsByIdHandler(String idNews, NewsService service) {
        if(idNews!=null){
            try{
                return service.getNewsById(idNews);
            }catch (Exception e){
                e.printStackTrace();
                return systemError(e);
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
                return systemError(e);
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
                return systemError(e);
            }

        }else {
            SimpleResponse response = new SimpleResponse();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }
    }

    private BaseResponse systemError(Exception e){
        SimpleResponse response = new SimpleResponse();
        response.setError(ErrorCode.SYSTEM_ERROR);
        response.setMsg("Lỗi hệ thống."+e.getMessage());
        return response;
    }
}
