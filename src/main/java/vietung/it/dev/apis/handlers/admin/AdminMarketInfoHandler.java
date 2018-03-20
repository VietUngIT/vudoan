package vietung.it.dev.apis.handlers.admin;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.handlers.BaseApiHandler;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.MarketInfoService;
import vietung.it.dev.core.services.imp.MarketInfoServiceImp;
import vietung.it.dev.core.utils.Utils;

public class AdminMarketInfoHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        MarketInfoService service = new MarketInfoServiceImp();
        String type = request.getFormAttribute("t");
        if(type!=null){
            if(type.equals("add")){
                String title = request.getFormAttribute("title");
                String image = request.getFormAttribute("image");
                String author = request.getFormAttribute("author");
                String source = request.getFormAttribute("source");
                String tags = request.getFormAttribute("tags");
                String idCateNews = request.getFormAttribute("idcatenews");
                String content = request.getFormAttribute("content");
                return createNewsMarketInfoHandler(title,author,image,source,tags,idCateNews,content,service);
            }else if(type.equals("edit")){
                String id = request.getFormAttribute("id");
                String title = request.getFormAttribute("title");
                String author = request.getFormAttribute("author");
                String source = request.getFormAttribute("source");
                String idCateNews = request.getFormAttribute("idcatenews");
                String content = request.getFormAttribute("content");
                return editNewsmarketInfoHandler(id,title,author,source,idCateNews,content,service);
            }else if(type.equals("editimage")){
                String id = request.getFormAttribute("id");
                String image = request.getFormAttribute("image");
                return editImageNewsMarketInfoHandler(id,image,service);
            }else if(type.equals("edittags")){
                String id = request.getFormAttribute("id");
                String tags = request.getFormAttribute("tags");
                return editTagsNewsMarketInfoHandler(id,tags,service);
            }else if(type.equals("del")){
                String id = request.getFormAttribute("id");
                if(id!=null){
                    return service.deleteNewsMarketInfo(id);
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

    private BaseResponse editTagsNewsMarketInfoHandler(String id, String tags, MarketInfoService service) {
        if(id!=null){
            if(tags==null){
                tags="[]";
            }
            try {
                return service.editTagsNewsMarketInfo(id,tags);
            } catch (Exception e) {
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.SYSTEM_ERROR,e.getMessage());
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse editImageNewsMarketInfoHandler(String id, String image, MarketInfoService service) throws Exception {
        if(id!=null && image!=null){
            return service.editImageNewsMarketInfo(id,image);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse editNewsmarketInfoHandler(String id, String title, String author, String source, String idCateNews, String content, MarketInfoService service) {
        if(id!=null){
            try {
                return service.editNewsmarketInfo(id,title,author,source,idCateNews,content);
            } catch (Exception e) {
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.SYSTEM_ERROR,e.getMessage());
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse createNewsMarketInfoHandler(String title, String author, String image, String source, String tags, String idCateNews, String content, MarketInfoService service) throws Exception {
        if(title!=null && content!=null && idCateNews!=null){
            if(tags==null){
                tags="[]";
            }
            return service.createNewsMarketInfo(title,author,image,source,tags,idCateNews,content);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
