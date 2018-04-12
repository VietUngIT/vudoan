package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.QAAnswerService;
import vietung.it.dev.core.services.imp.QAAnswerServiceImp;
import vietung.it.dev.core.utils.Utils;

public class QAAnswerHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        QAAnswerService service = new QAAnswerServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("getanswerbyidquestion")){
                String idQuestion = request.getParam("idquestion");
                String strofset = request.getParam("ofset");
                String strpage = request.getParam("page");
                return getQAAnswerByIDQuestionHandle(idQuestion,strofset,strpage,service);
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getQAAnswerByIDQuestionHandle(String idQuestion, String strofset, String strpage, QAAnswerService service) throws Exception {
        if (idQuestion!=null && strofset!=null && !idQuestion.trim().equals("") &&!strofset.trim().equals("")){
            if(strpage==null || strpage.trim().equals("")){
                strpage = "0";
            }
            try {
                int page = Integer.parseInt(strpage);
                int ofset = Integer.parseInt(strofset);
                return service.getQAAnswerByIDQuestion(idQuestion,page,ofset);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
