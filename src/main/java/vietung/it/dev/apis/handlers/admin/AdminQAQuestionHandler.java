package vietung.it.dev.apis.handlers.admin;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.handlers.BaseApiHandler;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.QAQuestionService;
import vietung.it.dev.core.services.imp.QAQuestionServiceImp;
import vietung.it.dev.core.utils.Utils;

public class AdminQAQuestionHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        QAQuestionService service = new QAQuestionServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("ad")){
                String idfield = request.getParam("idfield");
                String content = request.getParam("content");
                return addQAQuestionHandle(idfield,content,service);
            }else if(type.equals("edit")){
                String id = request.getParam("id");
                String idfield = request.getParam("idfield");
                String content = request.getParam("content");
                return editQAQuestionHandle(id,idfield,content,service);
            }else if(type.equals("del")){
                String id = request.getParam("id");
                return delQAQuestionHandle(id,service);
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse delQAQuestionHandle(String id, QAQuestionService service) throws Exception {
        if(id!=null && !id.trim().equals("")){
            return service.delQAQuestion(id);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse editQAQuestionHandle(String id, String idfield, String content, QAQuestionService service) throws Exception {
        if (id!=null && !id.trim().equals("") && idfield!=null && content!=null && !idfield.trim().equals("") && !content.trim().equals("")){
            return service.editQAQuestion(id,idfield,content);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse addQAQuestionHandle(String idfield, String content, QAQuestionService service) throws Exception{
        if (idfield!=null && content!=null && !idfield.trim().equals("") && !content.trim().equals("")){
            return service.addQAQuestion(idfield,content);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
