package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.MessagesService;
import vietung.it.dev.core.services.RoomService;
import vietung.it.dev.core.services.imp.MessagesServiceImp;
import vietung.it.dev.core.services.imp.RoomServiceImp;
import vietung.it.dev.core.utils.Utils;

public class RoomHandler extends BaseApiHandler {

    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        RoomService service = new RoomServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("create")){
                String name = request.getParam("name");
                String user = request.getParam("user");
                String typeRoom = request.getParam("type");
                return createRoom(name, user, typeRoom, service);
            }if(type.equals("edit")){
                String idroom = request.getParam("idroom");
                String name = request.getParam("name");
                String user = request.getParam("user");
                String typeRoom = request.getParam("type");
                return editRoom(idroom, name, user, typeRoom, service);
            }if(type.equals("delete")){
                String idroom = request.getParam("idroom");
                return deleteRoom(idroom,service);
            }if(type.equals("room_getbyiduser")){
                String iduser = request.getParam("iduser");
                String ofset = request.getParam("ofset");
                String strpage = request.getParam("page");
                return getAllRoomByIdUserWithRoom(strpage,iduser,ofset,service);
            }if(type.equals("room_getbyid")){
                String id = request.getParam("id");
                return getRoomById(id,service);
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse  createRoom(String name, String user, String typeRoom,RoomService service){
        if(name!=null && user!=null && typeRoom != null ){
            if(typeRoom == null){
                typeRoom= "0";
            }
            try {
                int type = Integer.parseInt(typeRoom);
                return service.createRoom(name,user,type);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }


    private BaseResponse  editRoom(String idroom,String name, String user, String typeRoom,RoomService service) {
        if (idroom != null) {
            int type = -1;
            if(typeRoom != null){
                try {
                   type = Integer.parseInt(typeRoom);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return Utils.notifiError(ErrorCode.CANT_CAST_TYPE, "Lỗi ép kiểu dữ liệu.");
                }
            }
            return service.editRoom(idroom, name, user,type);
        } else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS, "Invalid params.");
        }
    }

    private BaseResponse deleteRoom(String idroom,RoomService service){
        if(idroom!=null){
            return service.deleteRoom(idroom);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }


    private BaseResponse getAllRoomByIdUserWithRoom(String strpage, String iduser, String ofset, RoomService service) {
        if(ofset!=null && iduser!=null){
            if(strpage==null){
                strpage="0";
            }
            try {
                int page = Integer.parseInt(strpage);
                int of = Integer.parseInt(ofset);
                return service.getAllRoomByIdUserWithRoom(page,of,iduser);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getRoomById(String id, RoomService service) {
        if(id !=null){
            return service.getRoomById(id);
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

}
