package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.ExpertResponse;
import vietung.it.dev.apis.response.UserResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.Expert;
import vietung.it.dev.core.models.Users;
import vietung.it.dev.core.services.ExpertService;
import vietung.it.dev.core.services.UserService;
import vietung.it.dev.core.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ExpertServiceImp implements ExpertService {
    @Override
    public ExpertResponse addExpert(String name, String phone, String desc, String email, String address, String location, String field, String tags, String degree) throws Exception {
        ExpertResponse response = new ExpertResponse();
        UserResponse userResponse = new UserResponse();
        UserService service = new UserServiceImp();
        JsonArray arrayField = Utils.toJsonArray(field);
        JsonArray arrayTags = Utils.toJsonArray(tags);
        JsonArray arrayDegree = Utils.toJsonArray(degree);
        if(arrayField.size()<=0){
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Phải thêm lĩnh vực làm iệc cho chuyên gia");
            return response;
        }
        userResponse = service.register(name,phone,"123456",1);
        if(userResponse.getError()==0){
            List<String> lstIdField = new ArrayList<>();
            List<String> lstTags = new ArrayList<>();
            List<String> lstDegree = new ArrayList<>();
            for (int i=0;i<arrayField.size();i++){
                lstIdField.add(arrayField.get(i).getAsString());
            }
            for (int i=0;i<arrayTags.size();i++){
                lstTags.add(arrayTags.get(i).getAsString());
            }
            for (int i=0;i<arrayDegree.size();i++){
                lstDegree.add(arrayDegree.get(i).getAsString());
            }

            Expert expert = new Expert();
            ObjectId id = new ObjectId(userResponse.getUsers().get_id());
            expert.set_id(id.toString());
            expert.setName(name);
            expert.setPhone(phone);
            expert.setDesc(desc);
            expert.setEmail(email);
            expert.setAddress(address);
            expert.setLocation(location);
            expert.setIdFields(lstIdField);
            expert.setTags(lstTags);
            expert.setDegree(lstDegree);
            expert.setNumRate(0);
            expert.setRate((float)0);
            MongoPool.log(Expert.class.getSimpleName(),expert.toDocument());
            response.setData(expert.toJson());
            service.changeAddress(phone,address);
            return response;
        }else{
            response.setError(userResponse.getError());
            response.setMsg(userResponse.getMsg());
            return response;
        }
    }

    @Override
    public ExpertResponse deleteExpert(String phone) throws Exception {
        ExpertResponse response = new ExpertResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());
        MongoCollection collectionUser = jongo.getCollection(Users.class.getSimpleName());
        Users users = Utils.getUserByPhone(phone);
        if(users!=null){
            collection.remove(new ObjectId(users.get_id()));
            collectionUser.remove(new ObjectId(users.get_id()));
        }else {
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Số điện thoại không tồn tại.");
        }

        return response;
    }

    @Override
    public ExpertResponse editExpert(String phone, String desc, String location, String degree, String tags,String field) throws Exception {
        ExpertResponse response = new ExpertResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());
        Users users = Utils.getUserByPhone(phone);
        if(users!=null){
            MongoCursor<Expert> cursor = collection.find("{phone:#}",phone).limit(1).as(Expert.class);
            if(cursor.hasNext()){
                Expert expert = cursor.next();
                if(desc!=null){
                    collection.update("{phone:#}",phone).with("{$set:{desc:#}}",desc);
                    expert.setDesc(desc);
                }
                if(location!=null){
                    collection.update("{phone:#}",phone).with("{$set:{location:#}}",location);
                    expert.setLocation(location);
                }
                if(degree!=null){
                    JsonArray arrayDegree = Utils.toJsonArray(degree);
                    List<String> lstDegree = new ArrayList<>();
                    for (int i=0;i<arrayDegree.size();i++){
                        lstDegree.add(arrayDegree.get(i).getAsString());
                    }
                    collection.update("{phone:#}",phone).with("{$set:{degree:#}}",lstDegree);
                    expert.setDegree(lstDegree);
                }
                if(tags!=null){
                    JsonArray arrayTags = Utils.toJsonArray(tags);
                    List<String> lsttags = new ArrayList<>();
                    for (int i=0;i<arrayTags.size();i++){
                        lsttags.add(arrayTags.get(i).getAsString());
                    }
                    collection.update("{phone:#}",phone).with("{$set:{tags:#}}",lsttags);
                    expert.setTags(lsttags);
                }
                if(field!=null){
                    JsonArray arrayField = Utils.toJsonArray(field);
                    List<String> lstField = new ArrayList<>();
                    for (int i=0;i<arrayField.size();i++){
                        lstField.add(arrayField.get(i).getAsString());
                    }
                    collection.update("{phone:#}",phone).with("{$set:{idFields:#}}",lstField);
                    expert.setIdFields(lstField);
                }
                response.setData(expert.toJson());
            }else {
                response.setError(ErrorCode.USER_NOT_EXIST);
                response.setMsg("Chuyên gia này không tồn tại.");
            }

        }else {
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Số điện thoại không tồn tại.");
        }
        return response;
    }

    @Override
    public ExpertResponse editnameExpert(String phone, String nName) throws Exception {
        ExpertResponse response = new ExpertResponse();
        UserService service = new UserServiceImp();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());
        MongoCursor<Expert> cursor = collection.find("{phone:#}",phone).limit(1).as(Expert.class);
        if(cursor.hasNext()){
            Expert expert = cursor.next();
            collection.update("{phone:#}",phone).with("{$set:{name:#}}",nName);
            expert.setName(nName);
            service.changeName(phone,nName);
            response.setData(expert.toJson());
        }else{
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Chuyên gia này không tồn tại.");
        }
        return response;
    }

    @Override
    public ExpertResponse editPhoneExpert(String phone, String nPhone) throws Exception {
        ExpertResponse response = new ExpertResponse();
        UserService service = new UserServiceImp();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());
        MongoCursor<Expert> cursor = collection.find("{phone:#}",phone).limit(1).as(Expert.class);
        if(cursor.hasNext()){
            Expert expert = cursor.next();
            collection.update("{phone:#}",phone).with("{$set:{phone:#}}",nPhone);
            expert.setPhone(nPhone);
            service.changePhone(phone,nPhone);
            response.setData(expert.toJson());
        }else{
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Chuyên gia này không tồn tại.");
        }
        return response;
    }

    @Override
    public ExpertResponse editEmailExpert(String phone, String email) throws Exception {
        ExpertResponse response = new ExpertResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());
        MongoCursor<Expert> cursor = collection.find("{phone:#}",phone).limit(1).as(Expert.class);
        if(cursor.hasNext()){
            Expert expert = cursor.next();
            collection.update("{phone:#}",phone).with("{$set:{email:#}}",email);
            expert.setEmail(email);
            response.setData(expert.toJson());
        }else{
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Chuyên gia này không tồn tại.");
        }
        return response;
    }

    @Override
    public ExpertResponse editAddressExpert(String phone, String address) throws Exception {
        ExpertResponse response = new ExpertResponse();
        UserService service = new UserServiceImp();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());
        MongoCursor<Expert> cursor = collection.find("{phone:#}",phone).limit(1).as(Expert.class);
        if(cursor.hasNext()){
            Expert expert = cursor.next();
            collection.update("{phone:#}",phone).with("{$set:{address:#}}",address);
            expert.setAddress(address);
            service.changeAddress(phone,address);
            response.setData(expert.toJson());
        }else{
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Chuyên gia này không tồn tại.");
        }
        return response;
    }

}
