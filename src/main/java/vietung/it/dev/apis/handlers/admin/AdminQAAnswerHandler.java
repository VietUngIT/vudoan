package vietung.it.dev.apis.handlers.admin;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.handlers.BaseApiHandler;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.QAAnswerService;
import vietung.it.dev.core.services.imp.QAAnswerServiceImp;
import vietung.it.dev.core.utils.Utils;

public class AdminQAAnswerHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        QAAnswerService service = new QAAnswerServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("ad")){
                String idQuestion = request.getParam("idquestion");
                String content = request.getParam("content");
                return addQAAnswerHandle(idQuestion,content,service);
            }else if(type.equals("edit")){
                String id = request.getParam("id");
                String idQuestion = request.getParam("idquestion");
                String content = request.getParam("content");
                return editQAAnswerHandle(id,idQuestion,content,service);
            }else if(type.equals("del")){
                String id = request.getParam("id");
                return delQAAnswerHandle(id,service);
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse delQAAnswerHandle(String id, QAAnswerService service) throws Exception {
        if(id!=null && !id.trim().equals("")){
            return service.delQAAnswer(id);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse editQAAnswerHandle(String id, String idQuestion, String content, QAAnswerService service) throws Exception {
        if (id!=null && !id.trim().equals("") && idQuestion!=null && content!=null && !idQuestion.trim().equals("") && !content.trim().equals("")){
            return service.editQAAnswer(id,idQuestion,content);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse addQAAnswerHandle(String idQuestion, String content, QAAnswerService service) throws Exception {
        if (idQuestion!=null && content!=null && !idQuestion.trim().equals("") && !content.trim().equals("")){
            return service.addQAAnswer(idQuestion,content);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
