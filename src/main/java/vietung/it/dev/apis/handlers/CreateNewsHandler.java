package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.SimpleResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.NewsService;
import vietung.it.dev.core.services.imp.NewsServiceImp;

public class CreateNewsHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        NewsService service = new NewsServiceImp();
        String type = request.getFormAttribute("t");
        if(type!=null){
            if(type.equals("cr")){
                return null;
            }else if(type.equals("ed")){
                return null;
            }else if(type.equals("del")){
                String idNews = request.getFormAttribute("idn");
                if(idNews!=null){
                    return service.deleteNews(idNews);
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
