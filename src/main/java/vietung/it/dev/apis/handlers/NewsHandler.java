package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.SimpleResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.NewsService;
import vietung.it.dev.core.services.imp.NewsServiceImp;
import vietung.it.dev.core.utils.Utils;

public class NewsHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        NewsService service = new NewsServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("getall")){
                String timelast = request.getParam("timelast");
                String offer = request.getParam("offer");
                return getAllNewsHandler(timelast,offer,service);
            }if(type.equals("getbytype")){
                String timelast = request.getParam("timelast");
                String idtype = request.getParam("idtype");
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
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }


    private BaseResponse getAllNewsByTypeHandler(String timelast, String idtype, String offer, NewsService service) {
        if(timelast!=null && offer!=null && idtype!=null){
            try {
                long tlast = Long.parseLong(timelast);
                int of = Integer.parseInt(offer);
                return service.getAllNewsByType(tlast,of,idtype);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
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
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getNewsByIdHandler(String idNews, NewsService service) {
        if(idNews!=null){
            try{
                return service.getNewsById(idNews);
            }catch (Exception e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.SYSTEM_ERROR,"Lỗi hệ thống."+e.getMessage());
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse viewNewsHandler(String idNews, NewsService service) {
        if(idNews!=null){
            try{
                return service.viewNews(idNews);
            }catch (Exception e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.SYSTEM_ERROR,"Lỗi hệ thống."+e.getMessage());
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse likeNewsHandler(String like, String idNews,NewsService service){
        if(like!=null && idNews!=null){
            Boolean isLike = Boolean.parseBoolean(like);
            try{
                return service.likeNews(idNews,isLike);
            }catch (Exception e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.SYSTEM_ERROR,"Lỗi hệ thống."+e.getMessage());
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
