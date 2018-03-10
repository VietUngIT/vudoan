package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.SimpleResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.TypeNewsService;
import vietung.it.dev.core.services.imp.TypeNewsServiceImp;

public class CreateTypeNewsHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        TypeNewsService service = new TypeNewsServiceImp();
        String type = request.getFormAttribute("type");
        if(type != null){
            if(type.equals("ad")){
                String nameType = request.getFormAttribute("name");
                if(nameType!=null){
                    return service.addTypeNews(nameType);
                }else {
                    return invalidParams();
                }
            }else if(type.equals("ed")){
                String nameType = request.getFormAttribute("name");
                String id = request.getFormAttribute("id");
                if(nameType!=null && id!=null){
                    return service.editTypeNews(id,nameType);
                }else {
                    return invalidParams();
                }
            }else if(type.equals("del")){
                String id = request.getFormAttribute("id");
                if(id!=null){
                    return service.deleteTypeNews(id);
                }else {
                    return invalidParams();
                }
            }else {
                return invalidParams();
            }
        }else {
            return invalidParams();
        }
    }
    private BaseResponse invalidParams(){
        SimpleResponse response = new SimpleResponse();
        response.setError(ErrorCode.INVALID_PARAMS);
        response.setMsg("Invalid params.");
        return response;
    }
}
