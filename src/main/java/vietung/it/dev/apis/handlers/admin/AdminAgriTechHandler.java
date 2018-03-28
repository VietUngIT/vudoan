package vietung.it.dev.apis.handlers.admin;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.handlers.BaseApiHandler;
import vietung.it.dev.apis.response.AgriTechResponse;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.AgriTechService;
import vietung.it.dev.core.services.imp.AgriTechServiceImp;
import vietung.it.dev.core.utils.Utils;

public class AdminAgriTechHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        AgriTechService service = new AgriTechServiceImp();
        String type = request.getFormAttribute("t");
        if(type!=null){
            if(type.equals("add")){
                String title = request.getFormAttribute("title");
                String image = request.getFormAttribute("image");
                String author = request.getFormAttribute("author");
                String tags = request.getFormAttribute("tags");
                String idSubCate = request.getFormAttribute("idsubcate");
                String content = request.getFormAttribute("content");
                return createNewsAgriTechHandler(title,author,image,tags,idSubCate,content,service);
            }else if(type.equals("edit")){
                String id = request.getFormAttribute("id");
                String title = request.getFormAttribute("title");
                String author = request.getFormAttribute("author");
                String idSubCate = request.getFormAttribute("idsubcate");
                String content = request.getFormAttribute("content");
                return editNewsAgriTechHandler(id,title,author,idSubCate,content,service);
            }else if(type.equals("editimage")){
                String id = request.getFormAttribute("id");
                String image = request.getFormAttribute("image");
                return editImageNewsAgriTechHandler(id,image,service);
            }else if(type.equals("edittags")){
                String id = request.getFormAttribute("id");
                String tags = request.getFormAttribute("tags");
                return editTagsNewsAgriTechHandler(id,tags,service);
            }else if(type.equals("del")){
                String id = request.getFormAttribute("id");
                if(id!=null){
                    return service.deleteNewsAgriTech(id);
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

    private BaseResponse editTagsNewsAgriTechHandler(String id, String tags, AgriTechService service) {
        if(id!=null && !id.trim().equals("") ){
            if(tags==null || tags.trim().equals("") ){
                tags="[]";
            }
            try {
                return service.editTagsNewsAgriTech(id,tags);
            } catch (Exception e) {
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.SYSTEM_ERROR,e.getMessage());
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse editImageNewsAgriTechHandler(String id, String image, AgriTechService service) throws Exception {
        if(id!=null && image!=null){
            return service.editImageNewsAgriTech(id,image);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse editNewsAgriTechHandler(String id, String title, String author, String idSubCate, String content, AgriTechService service) {
        if(id!=null && !id.trim().equals("")){
            try {
                return service.editNewsAgriTech(id,title,author,idSubCate,content);
            } catch (Exception e) {
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.SYSTEM_ERROR,e.getMessage());
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse createNewsAgriTechHandler(String title, String author, String image, String tags, String idSubCate, String content, AgriTechService service) throws Exception {
        if(title!=null && content!=null && idSubCate!=null){
            if(tags==null || tags.trim().equals("")){
                tags="[]";
            }
            return service.createNewsAgriTech(title,author,image,tags,idSubCate,content);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }


}
