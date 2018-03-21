package vietung.it.dev.apis.handlers;

import io.vertx.core.http.HttpServerRequest;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.services.AgriTechService;
import vietung.it.dev.core.services.imp.AgriTechServiceImp;
import vietung.it.dev.core.utils.Utils;

public class AgriTechHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        AgriTechService service = new AgriTechServiceImp();
        String type = request.getParam("t");
        if(type!=null){
            if(type.equals("getbycate")){
                String idsubcate = request.getParam("idcate");
                String ofset = request.getParam("ofset");
                String strpage = request.getParam("page");
                return getAllNewsBySubCateWithNewestHandler(strpage,idsubcate,ofset,service);
            }else if(type.equals("get")){
                String id = request.getParam("id");
                return getNewsAgriTechByIdHandler(id,service);
            }else {
                return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
            }
        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getNewsAgriTechByIdHandler(String id, AgriTechService service) {
        if(id!=null){
            try{
                return service.getNewsAgriTechById(id);
            }catch (Exception e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.SYSTEM_ERROR,"Lỗi hệ thống."+e.getMessage());
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }

    private BaseResponse getAllNewsBySubCateWithNewestHandler(String strpage, String idsubcate, String ofset, AgriTechService service) throws Exception {
        if(ofset!=null && idsubcate!=null){
            if(strpage==null){
                strpage="0";
            }
            try {
                int page = Integer.parseInt(strpage);
                int ofs = Integer.parseInt(ofset);
                return service.getAllNewsAgriTechBySubCate(page,ofs,idsubcate);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.notifiError(ErrorCode.CANT_CAST_TYPE,"Lỗi ép kiểu dữ liệu.");
            }

        }else {
            return Utils.notifiError(ErrorCode.INVALID_PARAMS,"Invalid params.");
        }
    }
}
