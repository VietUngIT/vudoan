package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.MessagesResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.Messages;
import vietung.it.dev.core.services.MessagesService;
import vietung.it.dev.core.services.NewsService;
import vietung.it.dev.core.services.imp.MessagesServiceImp;
import vietung.it.dev.core.services.imp.NewsServiceImp;
import vietung.it.dev.core.utils.Utils;

public class MessagesHandler extends BaseApiHandler {

    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        MessagesService service = new MessagesServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("getbyroom")){
                String idroom = request.getParam("idroom");
                String ofset = request.getParam("ofset");
                String strpage = request.getParam("page");
                return getAllMessagesByRoomWithMessagesestHandler(strpage,idroom,ofset,service);
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getAllMessagesByRoomWithMessagesestHandler(String strpage, String idroom, String ofset, MessagesService service) {
        if(ofset!=null && idroom!=null){
            if(strpage==null){
                strpage="0";
            }
            try {
                int page = Integer.parseInt(strpage);
                int of = Integer.parseInt(ofset);
                return service.getAllMessagesByRoomWithMessagesest(page,of,idroom);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

}
