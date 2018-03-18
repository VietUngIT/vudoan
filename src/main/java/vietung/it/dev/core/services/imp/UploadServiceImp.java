package vietung.it.dev.core.services.imp;

import com.cloudinary.utils.ObjectUtils;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import vietung.it.dev.apis.response.UserResponse;
import vietung.it.dev.core.config.CloudinaryConfig;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.Users;
import vietung.it.dev.core.services.UploadService;
import vietung.it.dev.core.utils.Utils;

import java.io.IOException;
import java.util.Map;

public class UploadServiceImp implements UploadService {
    @Override
    public String uploadImage(String imageBase64) throws IOException {
        Map result = CloudinaryConfig.getInstance().uploader().upload(imageBase64, ObjectUtils.emptyMap());
        return result.get("url").toString();
    }

    @Override
    public UserResponse changeAvatar(String phone, String avatar) {
        UserResponse response = new UserResponse();
        try{
            Users users = Utils.getUserByPhone(phone);
            if(users != null){
                String urlAvatar = uploadImage(avatar);
                DB db =  MongoPool.getDBJongo();
                Jongo jongo = new Jongo(db);
                MongoCollection collection = jongo.getCollection(Users.class.getSimpleName());
                collection.update("{phone: #}",phone).with("{$set: {avatar: #}}",urlAvatar);
                users.setAvatar(urlAvatar);
                response.setUsers(users);
            }else {
                response.setError(ErrorCode.USER_NOT_EXIST);
                response.setMsg("Số điện thoại này chưa được đăng ký.");
            }
        }catch (IOException e){
            response.setError(ErrorCode.UPLOAD_IMAGE_ERROR);
            response.setMsg("Upload ảnh lỗi.nguyên nhân có thể do dữ liệu ảnh gửi lên bị sai.");
        }
        return response;
    }
}
