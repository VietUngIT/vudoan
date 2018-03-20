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
            if(type.equals("getbycate")){
                String idcate = request.getParam("idcate");
                String ofset = request.getParam("ofset");
                String with = request.getParam("with");
                String strpage = request.getParam("page");
                if(with!=null){
                    if(with.equals("popular")){
                        return getAllNewsByCateWithPopularHandler(strpage,idcate,ofset,service);
                    }else if(with.equals("favorite")){
                        return getAllNewsByCateWithFavoriteHandler(strpage,idcate,ofset,service);
                    }else{
                        return getAllNewsByCateWithNewestHandler(strpage,idcate,ofset,service);
                    }
                }else {
                    return getAllNewsByCateWithNewestHandler(strpage,idcate,ofset,service);
                }

            }else if(type.equals("get")){
                String idNews = request.getParam("id");
                return getNewsByIdHandler(idNews,service);
            }else if(type.equals("like")){
                String like = request.getParam("like");
                String idNews = request.getParam("id");
                return likeNewsHandler(like,idNews,service);
            }else if(type.equals("view")){
                String idNews = request.getParam("id");
                return viewNewsHandler(idNews,service);
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getAllNewsByCateWithPopularHandler(String strpage, String idcate, String ofset, NewsService service) {
        if(ofset!=null && idcate!=null){
            if(strpage==null){
                strpage="0";
            }
            try {
                int page = Integer.parseInt(strpage);
                int of = Integer.parseInt(ofset);
                return service.getAllNewsByCateWithPopular(page,of,idcate);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getAllNewsByCateWithFavoriteHandler(String strpage, String idcate, String ofset, NewsService service) {
        if(ofset!=null && idcate!=null){
            if(strpage==null){
                strpage="0";
            }
            try {
                int page = Integer.parseInt(strpage);
                int of = Integer.parseInt(ofset);
                return service.getAllNewsByCateWithFavorite(page,of,idcate);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getAllNewsByCateWithNewestHandler(String strpage, String idcate, String ofset, NewsService service) {
        if(ofset!=null && idcate!=null){
            if(strpage==null){
                strpage="0";
            }
            try {
                int page = Integer.parseInt(strpage);
                int of = Integer.parseInt(ofset);
                return service.getAllNewsByCateWithNewest(page,of,idcate);
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
