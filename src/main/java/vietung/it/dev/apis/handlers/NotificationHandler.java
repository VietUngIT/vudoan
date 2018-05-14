package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.NotificationService;
import vietung.it.dev.core.services.imp.NotificationServiceImp;
import vietung.it.dev.core.utils.Utils;

public class NotificationHandler  extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        NotificationService service = new NotificationServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("getbyidreceiver")){
                String idreceiver = request.getParam("idreceiver");
                String ofset = request.getParam("ofset");
                String strpage = request.getParam("page");
                return getAllNotificationByIdReceiverHandler(strpage,idreceiver,ofset,service);
            }if(type.equals("sendNoti")){
                String idSend = request.getParam("idSend");
                String idreceiver = request.getParam("idreceiver");
                String message = request.getParam("message");
                String action = request.getParam("action");
                String types = request.getParam("type");
                return sendNoti(idSend,idreceiver,message,action,types,service);
            } else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getAllNotificationByIdReceiverHandler(String strpage, String idreceiver, String ofset, NotificationService service) {
        if(ofset!=null && idreceiver!=null){
            if(strpage==null){
                strpage="0";
            }
            try {
                int page = Integer.parseInt(strpage);
                int of = Integer.parseInt(ofset);
                return service.getAllNotificationByIdReceiver(page,of,idreceiver);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse sendNoti(String idSend, String idreceiver,String message,String action,String types,NotificationService service){
        if(idSend!=null && idreceiver!=null && message != null && types != null){
            try {
                int type = Integer.parseInt(types);
                return service.sendNoti(idSend,idreceiver,message,action,type);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
