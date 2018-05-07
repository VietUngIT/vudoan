package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.QAQuestionService;
import vietung.it.dev.core.services.imp.QAQuestionServiceImp;
import vietung.it.dev.core.utils.Utils;

import java.util.Calendar;

public class QAQuestionHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        QAQuestionService service = new QAQuestionServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("getbyidfield")){
                String strofset = request.getParam("ofset");
                String strpage = request.getParam("page");
                String idfield = request.getParam("idfield");
                return getQAQuestionByIdFieldHandle(idfield,strofset,strpage,service);
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }



    private BaseResponse getQAQuestionByIdFieldHandle(String idfield, String strofset, String strpage, QAQuestionService service) throws Exception {
        if(idfield!=null && strofset!=null && !idfield.trim().equals("") && !strofset.trim().equals("")){
            if (strpage==null || strpage.trim().equals("")){
                strpage = "0";
            }
            try {
                int page = Integer.parseInt(strpage);
                int ofset = Integer.parseInt(strofset);
                return service.getQAQueationByIdField(page,ofset,idfield);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
