package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.SimpleResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.NewsService;
import vietung.it.dev.core.services.imp.NewsServiceImp;
import vietung.it.dev.core.utils.Utils;

public class CreateNewsHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        NewsService service = new NewsServiceImp();
        String type = request.getFormAttribute("t");
        if(type!=null){
            if(type.equals("cr")){
                String title = request.getFormAttribute("title");
                String shortDescription = request.getFormAttribute("shortdesc");
                String author = request.getFormAttribute("author");
                String image = request.getFormAttribute("image");
                String source = request.getFormAttribute("source");
                String tags = request.getFormAttribute("tags");
                String typeNews = request.getFormAttribute("typeNews");
                String nameTypeNews = request.getFormAttribute("nametype");
                String content = request.getFormAttribute("content");
                return createNewsHandler(title,shortDescription,author,image,source,tags,typeNews,nameTypeNews,content,service);
            }else if(type.equals("ed")){
                String idNews = request.getFormAttribute("id");
                String title = request.getFormAttribute("title");
                String shortDescription = request.getFormAttribute("shortdesc");
                String author = request.getFormAttribute("author");
                String image = request.getFormAttribute("image");
                String source = request.getFormAttribute("source");
                String tags = request.getFormAttribute("tags");
                String typeNews = request.getFormAttribute("typeNews");
                String nameTypeNews = request.getFormAttribute("nametype");
                String content = request.getFormAttribute("content");
                return editNewsHandler(idNews,title,shortDescription,author,image,source,tags,typeNews,nameTypeNews,content,service);
            }else if(type.equals("del")){
                String idNews = request.getFormAttribute("idn");
                if(idNews!=null){
                    return service.deleteNews(idNews);
                }else {
                    return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
                }
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse editNewsHandler(String idNews,String title, String shortDescription, String author, String image, String source, String tags, String typeNews,String nameTypeNews, String content, NewsService service) {
        if(idNews!=null && title!=null && shortDescription!=null && image!=null && typeNews!=null && content!=null && nameTypeNews!=null){
            return service.editNews(idNews,title,shortDescription,author,image,source,tags,typeNews,nameTypeNews,content);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse createNewsHandler(String title, String shortDescription, String author, String image, String source, String tags, String typeNews,String nameTypeNews, String content, NewsService service) {
        if(title!=null && shortDescription!=null && image!=null && typeNews!=null && content!=null && nameTypeNews!=null){
            return service.createNews(title,shortDescription,author,image,source,tags,typeNews,nameTypeNews,content);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse systemError(Exception e){
        SimpleResponse response = new SimpleResponse();
        response.setError(ErrorCode.SYSTEM_ERROR);
        response.setMsg("Lỗi hệ thống."+e.getMessage());
        return response;
    }
}
