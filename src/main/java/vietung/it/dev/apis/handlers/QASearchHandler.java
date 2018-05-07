package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.QAQuestionService;
import vietung.it.dev.core.services.imp.QAQuestionServiceImp;
import vietung.it.dev.core.utils.Utils;

public class QASearchHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        QAQuestionService service = new QAQuestionServiceImp();
        String type = request.getFormAttribute("t");
        if(type!=null){
            if(type.equals("searchqa")){
                String content = request.getFormAttribute("content");
                String id = request.getFormAttribute("id");
                return searchQAHandel(content,id, service);
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
    private BaseResponse searchQAHandel(String content,String id, QAQuestionService service) throws Exception {
        if(id!=null){
            return service.searchQA(content,id);
        }else{
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
