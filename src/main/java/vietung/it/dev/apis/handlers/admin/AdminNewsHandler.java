package vietung.it.dev.apis.handlers.admin;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.handlers.BaseApiHandler;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.NewsService;
import vietung.it.dev.core.services.imp.NewsServiceImp;
import vietung.it.dev.core.utils.Utils;

public class AdminNewsHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        NewsService service = new NewsServiceImp();
        String type = request.getFormAttribute("t");
        if(type!=null){
            if(type.equals("add")){
                String title = request.getFormAttribute("title");
                String shortDescription = request.getFormAttribute("shortdesc");
                String author = request.getFormAttribute("author");
                String image = request.getFormAttribute("image");
                String source = request.getFormAttribute("source");
                String tags = request.getFormAttribute("tags");
                String idCateNews = request.getFormAttribute("idcatenews");
                String content = request.getFormAttribute("content");
                return createNewsHandler(title,shortDescription,author,image,source,tags,idCateNews,content,service);
            }else if(type.equals("edit")){
                String idNews = request.getFormAttribute("id");
                String title = request.getFormAttribute("title");
                String shortDescription = request.getFormAttribute("shortdesc");
                String author = request.getFormAttribute("author");
                String source = request.getFormAttribute("source");
                String idCateNews = request.getFormAttribute("idcatenews");
                String content = request.getFormAttribute("content");
                return editNewsHandler(idNews,title,shortDescription,author,source,idCateNews,content,service);
            }else if(type.equals("editimage")){
                String idNews = request.getFormAttribute("id");
                String image = request.getFormAttribute("image");
                return editImageNewsHandler(idNews,image,service);
            }else if(type.equals("edittags")){
                String idNews = request.getFormAttribute("id");
                String tags = request.getFormAttribute("tags");
                return editTagsNewsHandler(idNews,tags,service);
            }else if(type.equals("del")){
                String idNews = request.getFormAttribute("id");
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

    private BaseResponse editTagsNewsHandler(String idNews, String tags, NewsService service) {
        if(idNews!=null){
            if(tags==null){
                tags="[]";
            }
            return service.editTagsNews(idNews,tags);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse editImageNewsHandler(String idNews, String image, NewsService service) {
        if(idNews!=null && image!=null){
            return service.editImageNews(idNews,image);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse editNewsHandler(String idNews, String title, String shortDescription, String author, String source,String idCateNews,String content, NewsService service) {
        if(idNews!=null){
            return service.editNews(idNews,title,shortDescription,author,source,idCateNews,content);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse createNewsHandler(String title, String shortDescription, String author, String image, String source, String tags, String idCateNews, String content, NewsService service) throws Exception {
        if(title!=null && shortDescription!=null && content!=null && idCateNews!=null){
            if(tags==null){
                tags="[]";
            }
            return service.createNews(title,shortDescription,author,image,source,tags,idCateNews,content);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
