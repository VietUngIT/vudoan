package vietung.it.dev.core.services.imp;

import com.mongodb.DB;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.UserResponse;
import vietung.it.dev.core.config.MongoConfig;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.Users;
import vietung.it.dev.core.services.UserService;
import vietung.it.dev.core.utils.Utils;

import java.util.Calendar;

public class UserServiceImp implements UserService {
    @Override
    public UserResponse register(String name, String phone, String pass, int role) {
        UserResponse response = new UserResponse();
        Users users = Utils.getUserByPhone(phone);
        if(users == null){
            Calendar calendar = Calendar.getInstance();
            Users user = new Users();
            user.setName(name);
            user.setPhone(phone);
            user.setPassword(Utils.sha256(pass));
            user.setRoles(role);
            user.setCreateTime(calendar.getTimeInMillis());
            MongoPool.log(Users.class.getSimpleName(),user.toDocument());
            response.setUsers(user);
        }else {
            response.setError(ErrorCode.DUPLICATE_USER);
            response.setMsg("Số điện thoại này đã được đăng ký.");
        }
        return response;
    }

    @Override
    public UserResponse login(String phone, String pass) {
        UserResponse response = new UserResponse();
        Users users = Utils.getUserByPhone(phone);
        if(users != null){
            if(users.getPassword().equals(Utils.sha256(pass))){
                response.setUsers(users);
            }else {
                response.setError(ErrorCode.INVALID_PASSWORD);
                response.setMsg("Mật khẩu không đúng.");
            }
        }else {
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Số điện thoại này chưa được đăng ký.");
        }
        return response;
    }

    @Override
    public UserResponse changePass(String phone, String oldPass, String newPass) {
        UserResponse response = new UserResponse();
        Users users = Utils.getUserByPhone(phone);
        if(users != null){
            if(users.getPassword().equals(Utils.sha256(oldPass))){
                DB db =  MongoPool.getDBJongo();
                Jongo jongo = new Jongo(db);
                MongoCollection collection = jongo.getCollection(Users.class.getSimpleName());
                collection.update("{phone: #}",phone).with("{$set: {password: #}}",Utils.sha256(newPass));
                users.setPassword(Utils.sha256(newPass));
                response.setUsers(users);
            }else {
                response.setError(ErrorCode.INVALID_PASSWORD);
                response.setMsg("Mật khẩu không đúng.");
            }
        }else {
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Số điện thoại này chưa được đăng ký.");
        }
        return response;
    }

    @Override
    public UserResponse changePhone(String oldPhone, String newPhone, String pass) {
        UserResponse response = new UserResponse();
        Users users = Utils.getUserByPhone(oldPhone);
        if(users != null){
            if(users.getPassword().equals(Utils.sha256(pass))){
                DB db =  MongoPool.getDBJongo();
                Jongo jongo = new Jongo(db);
                MongoCollection collection = jongo.getCollection(Users.class.getSimpleName());
                collection.update("{phone: #}",oldPhone).with("{$set: {phone: #}}",newPhone);
                users.setPhone(newPhone);
                response.setUsers(users);
            }else {
                response.setError(ErrorCode.INVALID_PASSWORD);
                response.setMsg("Mật khẩu không đúng.");
            }
        }else {
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Số điện thoại này chưa được đăng ký.");
        }
        return response;
    }

    @Override
    public UserResponse changeRole(String phone, String pass, int role) {
        UserResponse response = new UserResponse();
        Users users = Utils.getUserByPhone(phone);
        if(users != null){
            if(users.getPassword().equals(Utils.sha256(pass))){
                DB db =  MongoPool.getDBJongo();
                Jongo jongo = new Jongo(db);
                MongoCollection collection = jongo.getCollection(Users.class.getSimpleName());
                collection.update("{phone: #}",phone).with("{$set: {roles: #}}",role);
                users.setRoles(role);
                response.setUsers(users);
            }else {
                response.setError(ErrorCode.INVALID_PASSWORD);
                response.setMsg("Mật khẩu không đúng.");
            }
        }else {
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Số điện thoại này chưa được đăng ký.");
        }
        return response;
    }

    @Override
    public UserResponse getUserInfor(String phone) {
        UserResponse response = new UserResponse();
        Users users = Utils.getUserByPhone(phone);
        if(users != null){
            response.setUsers(users);
        }else {
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Số điện thoại này chưa được đăng ký.");
        }
        return response;
    }
}
