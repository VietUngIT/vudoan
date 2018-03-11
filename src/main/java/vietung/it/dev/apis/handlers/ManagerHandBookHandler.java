package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.HandbookService;
import vietung.it.dev.core.services.imp.HandbookServiceImp;
import vietung.it.dev.core.utils.Utils;

public class ManagerHandBookHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        HandbookService service = new HandbookServiceImp();
        String type = request.getFormAttribute("t");
        if(type!=null){
            if(type.equals("cr")){
                String title = request.getFormAttribute("title");
                String author = request.getFormAttribute("author");
                String image = request.getFormAttribute("image");
                String typeNews = request.getFormAttribute("typeNews");
                String nameTypeNews = request.getFormAttribute("nametype");
                String content = request.getFormAttribute("content");
                return createHandbookHandler(title,author,image,typeNews,nameTypeNews,content,service);
            }else if(type.equals("ed")){
                String idHB = request.getFormAttribute("id");
                String title = request.getFormAttribute("title");
                String author = request.getFormAttribute("author");
                String image = request.getFormAttribute("image");
                String typeNews = request.getFormAttribute("typeNews");
                String nameTypeNews = request.getFormAttribute("nametype");
                String content = request.getFormAttribute("content");
                return editHandbookHandler(idHB,title,author,image,typeNews,nameTypeNews,content,service);
            }else if(type.equals("del")){
                String idHB = request.getFormAttribute("id");
                if(idHB!=null){
                    return service.deleteHandbook(idHB);
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

    private BaseResponse editHandbookHandler(String idHB, String title, String author, String image, String typeNews,String nameTypeNews, String content, HandbookService service) {
        if(idHB!=null && title!=null && image!=null && typeNews!=null && content!=null && nameTypeNews!=null){
            return service.editHandbook(idHB,title,author,image,typeNews,nameTypeNews,content);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse createHandbookHandler(String title, String author, String image, String typeNews,String nameTypeNews, String content, HandbookService service) {
        if(title!=null && image!=null && typeNews!=null && content!=null && nameTypeNews!=null){
            return service.createHandbook(title,author,image,typeNews,nameTypeNews,content);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
