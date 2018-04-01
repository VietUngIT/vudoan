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
import vietung.it.dev.core.consts.Variable;
import vietung.it.dev.core.models.Category;
import vietung.it.dev.core.models.Expert;
import vietung.it.dev.core.models.Users;
import vietung.it.dev.core.services.ExpertService;
import vietung.it.dev.core.services.UserService;
import vietung.it.dev.core.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpertServiceImp implements ExpertService {
    @Override
    public ExpertResponse addExpert(String name, String phone, String desc, String email, String address, Double lat,Double lon, String field, String tags, String degree) throws Exception {
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
                String idField = arrayField.get(i).getAsString();
                if (ObjectId.isValid(idField)) {
                    lstIdField.add(idField);
                }
            }
            for (int i=0;i<arrayTags.size();i++){
                lstTags.add(arrayTags.get(i).getAsString().toLowerCase());
            }
            for (int i=0;i<arrayDegree.size();i++){
                lstDegree.add(arrayDegree.get(i).getAsString().toLowerCase());
            }

            Expert expert = new Expert();
            ObjectId id = new ObjectId(userResponse.getUsers().get_id());
            expert.set_id(id.toString());
            expert.setName(name);
            expert.setPhone(phone);
            expert.setDesc(desc);
            expert.setEmail(email);
            expert.setAddress(address);
            expert.setLat(lat);
            expert.setLon(lon);
            expert.setIdFields(lstIdField);
            expert.setTags(lstTags);
            expert.setDegree(lstDegree);
            expert.setNumRate(0);
            expert.setRate((float)0);
            expert.setIsOnline(false);
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
    public ExpertResponse editExpert(String phone, String desc, Double lat,Double lon, String degree, String tags,String field) throws Exception {
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
                if(lat!=null && lon!=null){
                    collection.update("{phone:#}",phone).with("{$set:{lat:#,lon:#}}",lat,lon);
                    expert.setLat(lat);
                    expert.setLon(lon);
                }
                if(degree!=null){
                    JsonArray arrayDegree = Utils.toJsonArray(degree);
                    List<String> lstDegree = new ArrayList<>();
                    for (int i=0;i<arrayDegree.size();i++){
                        lstDegree.add(arrayDegree.get(i).getAsString().toLowerCase());
                    }
                    collection.update("{phone:#}",phone).with("{$set:{degree:#}}",lstDegree);
                    expert.setDegree(lstDegree);
                }
                if(tags!=null){
                    JsonArray arrayTags = Utils.toJsonArray(tags);
                    List<String> lsttags = new ArrayList<>();
                    for (int i=0;i<arrayTags.size();i++){
                        lsttags.add(arrayTags.get(i).getAsString().toLowerCase());
                    }
                    collection.update("{phone:#}",phone).with("{$set:{tags:#}}",lsttags);
                    expert.setTags(lsttags);
                }
                if(field!=null){
                    JsonArray arrayField = Utils.toJsonArray(field);
                    List<String> lstField = new ArrayList<>();
                    for (int i=0;i<arrayField.size();i++){
                        String idField = arrayField.get(i).getAsString();
                        if (ObjectId.isValid(idField)) {
                            lstField.add(idField);
                        }
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

    @Override
    public ExpertResponse updateStatusOnlineExpert(Boolean isOnline, String id) throws Exception {
        ExpertResponse response = new ExpertResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());
        MongoCursor<Expert> cursor = collection.find("{_id:#}",new ObjectId(id)).limit(1).as(Expert.class);
        if(cursor.hasNext()){
            Expert expert = cursor.next();
            collection.update("{_id:#}",new ObjectId(id)).with("{$set:{isOnline:#}}",isOnline);
            expert.setIsOnline(isOnline);
            response.setData(expert.toJson());
        }else {
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Chuyên gia này không tồn tại.");
        }
        return response;
    }

    @Override
    public ExpertResponse getInfoExpert(String id) throws Exception {
        ExpertResponse response = new ExpertResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());
        MongoCursor<Expert> cursor = collection.find("{_id:#}",new ObjectId(id)).limit(1).as(Expert.class);
        if(cursor.hasNext()){
            Expert expert = cursor.next();
            expert.setNameFields(getListNameFieldOfExpert(expert.getIdFields()));
            response.setData(expert.toJson());
        }else {
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Chuyên gia này không tồn tại.");
        }
        return response;
    }

    @Override
    public ExpertResponse getListExpertNearest(Double lat, Double lon, int numExpert, String field) throws Exception {
        ExpertResponse response = new ExpertResponse();
        if (!ObjectId.isValid(field)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ idFields: { $elemMatch: { $eq:# } } }");
        MongoCursor<Expert> cursor = collection.find(stringBuilder.toString(),field).as(Expert.class);
        List<Expert> lstExpert = new ArrayList<>();
        while(cursor.hasNext()){
            Expert expert = cursor.next();
            expert.setNameFields(getListNameFieldOfExpert(expert.getIdFields()));
            double dist = Utils.distance(lat,lon,expert.getLat(),expert.getLon(),"K");
            expert.setDistance(dist);
            lstExpert.add(expert);
        }
        Collections.sort(lstExpert,Expert.DISTANCE_ASC);
        JsonArray array = new JsonArray();
        for (int i=0;i<lstExpert.size();i++){
            if(i==numExpert) break;
            array.add(lstExpert.get(i).toJson());
        }
        response.setArray(array);
        return response;
    }

    @Override
    public ExpertResponse editTagsExpert(String phone, String tags) throws Exception {
        ExpertResponse response = new ExpertResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());
        MongoCursor<Expert> cursor = collection.find("{phone:#}",phone).limit(1).as(Expert.class);
        if(cursor.hasNext()){
            Expert expert = cursor.next();
            JsonArray arrayTags = Utils.toJsonArray(tags);
            List<String> lsttags = new ArrayList<>();
            for (int i=0;i<arrayTags.size();i++){
                lsttags.add(arrayTags.get(i).getAsString().toLowerCase());
            }
            collection.update("{phone:#}",phone).with("{$set:{tags:#}}",lsttags);
            expert.setTags(lsttags);
            response.setData(expert.toJson());
        }else{
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Chuyên gia này không tồn tại.");
        }
        return response;
    }

    private JsonArray getListNameFieldOfExpert(List<String> idField){
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Variable.MG_CATEGORY_FIELD_EXPERT);

        List<ObjectId> ids = new ArrayList<ObjectId>();
        for (int i=0;i<idField.size();i++){
            ids.add(new ObjectId(idField.get(i)));
        }
        MongoCursor<Category> cursor = collection.find("{_id:{$in:#}}", ids).as(Category.class);
        JsonArray array = new JsonArray();
        while (cursor.hasNext()){
            array.add(cursor.next().toJson());
        }
        return array;
    }
}
