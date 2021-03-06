package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Aggregate;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.ExpertResponse;
import vietung.it.dev.apis.response.UserResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.consts.Variable;
import vietung.it.dev.core.models.*;
import vietung.it.dev.core.services.ExpertService;
import vietung.it.dev.core.services.UserService;
import vietung.it.dev.core.utils.Utils;

import java.util.*;

public class ExpertServiceImp implements ExpertService {
    private final static long BLOCK_DATE = 86400000;
    @Override
    public ExpertResponse addExpert(String name, String phone, String desc, String email, String address,String idParentField, String field, String tags, String degree, String workplace) throws Exception {
        ExpertResponse response = new ExpertResponse();
        UserResponse userResponse = new UserResponse();
        UserService service = new UserServiceImp();
        JsonArray arrayField = Utils.toJsonArray(field);
        JsonArray arrayTags = Utils.toJsonArray(tags);
        JsonArray arrayDegree = Utils.toJsonArray(degree);
        if (!ObjectId.isValid(idParentField)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id field không đúng.");
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
            expert.setIdUser(userResponse.getUsers().get_id());
            expert.setPhone(phone);
            expert.setDesc(desc);
            expert.setEmail(email);
            expert.setAddress(address);
            expert.setWorkPlace(workplace);
            expert.setIdParentField(idParentField);
            expert.setIdFields(lstIdField);
            expert.setTags(lstTags);
            expert.setDegree(lstDegree);
            expert.setNumRate(0);
            expert.setRate((float)0);
            expert.setIsOnline(false);
            MongoPool.log(Expert.class.getSimpleName(),expert.toDocument());
            response.setData(expert.toJson());
            service.changeAddress(phone,address);
            if(expert.getEmail()!=null && !expert.getEmail().trim().equals("")){
                Utils.sendEmail(expert.getEmail().trim(),expert.getName(),expert.getPhone());
            }
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
    public ExpertResponse editExpert(String phone, String desc, Double lat,Double lon, String email, String idParentField, String workplace) throws Exception {
        ExpertResponse response = new ExpertResponse();
        if (!ObjectId.isValid(idParentField)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
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
                if(lat!=null && lon!=null){
                    collection.update("{phone:#}",phone).with("{$set:{email:#}}",email);
                    expert.setLat(lat);
                    expert.setLon(lon);
                }
                if(workplace!=null){
                    collection.update("{phone:#}",phone).with("{$set:{workPlace:#}}",workplace);
                }
                collection.update("{phone:#}",phone).with("{$set:{idParentField:#}}",idParentField);
                expert.setIdParentField(idParentField);
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
    public ExpertResponse updateStatusOnlineExpert(Boolean isOnline, String id,Double lat,Double lon) throws Exception {
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
            if(isOnline){
                collection.update("{_id:#}",new ObjectId(id)).with("{$set:{isOnline:#,lat:#,lon:#}}",isOnline,lat,lon);
                expert.setLat(lat);
                expert.setLon(lon);
            }else{
                collection.update("{_id:#}",new ObjectId(id)).with("{$set:{isOnline:#}}",isOnline);
            }
            expert.setIsOnline(isOnline);
            response.setData(expert.toJson());
        }else {
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Chuyên gia này không tồn tại.");
        }
        return response;
    }

    @Override
    public ExpertResponse getInfoExpert(String id, String phone) throws Exception {
        ExpertResponse response = new ExpertResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        Users users = Utils.getUserByPhone(phone);
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());
        MongoCursor<Expert> cursor = collection.find("{_id:#}",new ObjectId(id)).limit(1).as(Expert.class);
        if(cursor.hasNext()){
            Expert expert = cursor.next();
            expert.setNameFields(getListNameFieldOfExpert(expert.getIdFields()));
            if(expert.getUsersRate()!=null){
                for (UserRate u: expert.getUsersRate()){
                    if(u.getUser().equals(users.get_id())){
                        expert.setIsRated(u.getRate());
                    }
                }
            }
            response.setData(expert.toJson());
        }else {
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Chuyên gia này không tồn tại.");
        }
        return response;
    }

    @Override
    public ExpertResponse getListExpertNearest(Double lat, Double lon, int numExpert, String idparentfieldExpert) throws Exception {
        ExpertResponse response = new ExpertResponse();
        if (!ObjectId.isValid(idparentfieldExpert)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());
        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("{ idFields: { $elemMatch: { $eq:# } } }");
        stringBuilder.append("{ idParentField: # }");
        MongoCursor<Expert> cursor = collection.find(stringBuilder.toString(),idparentfieldExpert).as(Expert.class);
        List<Expert> lstExpert = new ArrayList<>();
        while(cursor.hasNext()){
            Expert expert = cursor.next();
            if(expert.getLat()>0 && expert.getLon()>0){
                expert.setNameFields(getListNameFieldOfExpert(expert.getIdFields()));
                double dist = Utils.distance(lat,lon,expert.getLat(),expert.getLon(),"K");
                expert.setDistance(dist);
                lstExpert.add(expert);
            }

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
            expert.setNameFields(getListNameFieldOfExpert(expert.getIdFields()));
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

    @Override
    public ExpertResponse editDegreeExpert(String phone, String degree) throws Exception {
        ExpertResponse response = new ExpertResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());
        MongoCursor<Expert> cursor = collection.find("{phone:#}",phone).limit(1).as(Expert.class);
        if(cursor.hasNext()){
            Expert expert = cursor.next();
            expert.setNameFields(getListNameFieldOfExpert(expert.getIdFields()));
            JsonArray arrayTags = Utils.toJsonArray(degree);
            List<String> lstdegree = new ArrayList<>();
            for (int i=0;i<arrayTags.size();i++){
                lstdegree.add(arrayTags.get(i).getAsString().toLowerCase());
            }
            collection.update("{phone:#}",phone).with("{$set:{degree:#}}",lstdegree);
            expert.setDegree(lstdegree);
            response.setData(expert.toJson());
        }else{
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Chuyên gia này không tồn tại.");
        }
        return response;
    }

    @Override
    public ExpertResponse editFieldExpert(String phone, String idfield) throws Exception {
        ExpertResponse response = new ExpertResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());
        MongoCursor<Expert> cursor = collection.find("{phone:#}",phone).limit(1).as(Expert.class);
        if(cursor.hasNext()){
            Expert expert = cursor.next();
            JsonArray arrayTags = Utils.toJsonArray(idfield);

            List<String> lstIdField = new ArrayList<>();
            for (int i=0;i<arrayTags.size();i++){
                lstIdField.add(arrayTags.get(i).getAsString().toLowerCase());
            }
            collection.update("{phone:#}",phone).with("{$set:{idFields:#}}",lstIdField);
            expert.setIdFields(lstIdField);
            expert.setNameFields(getListNameFieldOfExpert(expert.getIdFields()));
            response.setData(expert.toJson());
        }else{
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Chuyên gia này không tồn tại.");
        }
        return response;
    }

    @Override
    public ExpertResponse rateExpert(String id, int rate, String phone) throws Exception {
        ExpertResponse response = new ExpertResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
        }
        Users users = Utils.getUserByPhone(phone);
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());
        MongoCursor<Expert> cursor = collection.find("{_id:#}",new ObjectId(id)).limit(1).as(Expert.class);
        if(cursor.hasNext()){
            Expert expert = cursor.next();
            UserRate userRate = null;
            if(expert.getUsersRate()!=null){
                for (UserRate u: expert.getUsersRate()){
                    if(u.getUser().equals(users.get_id())){
//                        userRate = new UserRate();
                        userRate = u;
                        updateNumRate(userRate,jongo,expert);
                    }
                }
            }

            int numRate = expert.getNumRate();
            float newRate = caculatoRate(expert,rate, userRate);
            if(rate==1){
                int r1 = expert.getNumRate1();
                collection.update("{_id:#}",new ObjectId(id)).with("{$set:{numRate:#,rate:#,numRate1: #}}",userRate==null?(++numRate):numRate,newRate,++r1);
                expert.setNumRate1(r1);
            }else if(rate==2){
                int r2 = expert.getNumRate2();
                collection.update("{_id:#}",new ObjectId(id)).with("{$set:{numRate:#,rate:#,numRate2: #}}",userRate==null?(++numRate):numRate,newRate,++r2);
                expert.setNumRate2(r2);
            }else if(rate==3){
                int r3 = expert.getNumRate3();
                collection.update("{_id:#}",new ObjectId(id)).with("{$set:{numRate:#,rate:#,numRate3: #}}",userRate==null?(++numRate):numRate,newRate,++r3);
                expert.setNumRate3(r3);
            }else if(rate==4){
                int r4 = expert.getNumRate4();
                collection.update("{_id:#}",new ObjectId(id)).with("{$set:{numRate:#,rate:#,numRate4: #}}",userRate==null?(++numRate):numRate,newRate,++r4);
                expert.setNumRate4(r4);
            }else if(rate==5){
                int r5 = expert.getNumRate5();
                collection.update("{_id:#}",new ObjectId(id)).with("{$set:{numRate:#,rate:#,numRate5: #}}",userRate==null?(++numRate):numRate,newRate,++r5);
                expert.setNumRate5(r5);
            }
            if(userRate==null){
                userRate = new UserRate();
                userRate.setRate(rate);
                userRate.setUser(users.get_id());
                collection.update("{_id:#}", new ObjectId(id)).with("{ $push: {usersRate:#}}",userRate.toDocument());
            }else{
                collection.update("{_id:#,usersRate.user: #}", new ObjectId(id),userRate.getUser()).with("{$set:{usersRate.$.rate:#}}",rate);
            }
            expert.setNumRate(numRate);
            expert.setRate(newRate);
            JsonObject object = new JsonObject();
            object.addProperty("numRate",numRate);
            object.addProperty("rate",newRate);
            response.setData(object);
        }else{
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Chuyên gia này không tồn tại.");
        }
        return response;
    }

    private void updateNumRate(UserRate userRate,Jongo jongo, Expert expert){
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());

        switch (userRate.getRate()){
            case 1:
                int n1 = expert.getNumRate1();
                collection.update("{_id:#}",new ObjectId(expert.get_id())).with("{$set:{numRate1: #}}",--n1);
//                expert.setNumRate1(n1);
                break;
            case 2:
                int n2 = expert.getNumRate2();
                collection.update("{_id:#}",new ObjectId(expert.get_id())).with("{$set:{numRate2: #}}",--n2);
//                expert.setNumRate2(n2);
                break;
            case 3:
                int n3 = expert.getNumRate3();
                collection.update("{_id:#}",new ObjectId(expert.get_id())).with("{$set:{numRate3: #}}",--n3);
//                expert.setNumRate3(n3);
                break;
            case 4:
                int n4 = expert.getNumRate4();
                collection.update("{_id:#}",new ObjectId(expert.get_id())).with("{$set:{numRate4: #}}",--n4);
//                expert.setNumRate4(n4);
                break;
            case 5:
                int n5 = expert.getNumRate5();
                collection.update("{_id:#}",new ObjectId(expert.get_id())).with("{$set:{numRate5: #}}",--n5);
//                expert.setNumRate5(n5);
                break;
        }
    }

    private float caculatoRate(Expert e, int rate, UserRate userRate){
        if (userRate==null){
            float res = 0;
            float ts = 0;
            switch (rate){
                case 1:
                    ts = ((e.getNumRate1()+1)+e.getNumRate2()*2+e.getNumRate3()*3+e.getNumRate4()*4+e.getNumRate5()*5);
                    res = ts/(float)(e.getNumRate()+1);
                    break;
                case 2:
                    ts = (e.getNumRate1()+(e.getNumRate2()+1)*2+e.getNumRate3()*3+e.getNumRate4()*4+e.getNumRate5()*5);
                    res = ts/(float)(e.getNumRate()+1);
                    break;
                case 3:
                    ts = (e.getNumRate1()+e.getNumRate2()*2+(e.getNumRate3()+1)*3+e.getNumRate4()*4+e.getNumRate5()*5);
                    res = ts/(float)(e.getNumRate()+1);
                    break;
                case 4:
                    ts = (e.getNumRate1()+e.getNumRate2()*2+e.getNumRate3()*3+(e.getNumRate4()+1)*4+e.getNumRate5()*5);
                    res = ts/(float)(e.getNumRate()+1);
                    break;
                case 5:
                    ts = (e.getNumRate1()+e.getNumRate2()*2+e.getNumRate3()*3+e.getNumRate4()*4+(e.getNumRate5()+1)*5);
                    res = ts/(float)(e.getNumRate()+1);
                    break;
            }
            float tempRes = res*10;
            float kq = Math.round(tempRes);
            return kq / 10;
        }else {
            float res = 0;
            float ts = 0;
            switch (rate){
                case 1:
                    ts = ((e.getNumRate1()+1)+e.getNumRate2()*2+e.getNumRate3()*3+e.getNumRate4()*4+e.getNumRate5()*5 - userRate.getRate());
                    res = ts/(float)(e.getNumRate());
                    break;
                case 2:
                    ts = (e.getNumRate1()+(e.getNumRate2()+1)*2+e.getNumRate3()*3+e.getNumRate4()*4+e.getNumRate5()*5 - userRate.getRate());
                    res = ts/(float)(e.getNumRate());
                    break;
                case 3:
                    ts = (e.getNumRate1()+e.getNumRate2()*2+(e.getNumRate3()+1)*3+e.getNumRate4()*4+e.getNumRate5()*5 - userRate.getRate());
                    res = ts/(float)(e.getNumRate());
                    break;
                case 4:
                    ts = (e.getNumRate1()+e.getNumRate2()*2+e.getNumRate3()*3+(e.getNumRate4()+1)*4+e.getNumRate5()*5 - userRate.getRate());
                    res = ts/(float)(e.getNumRate());
                    break;
                case 5:
                    ts = (e.getNumRate1()+e.getNumRate2()*2+e.getNumRate3()*3+e.getNumRate4()*4+(e.getNumRate5()+1)*5 - userRate.getRate());
                    res = ts/(float)(e.getNumRate());
                    break;
            }

            float tempRes = res*10;
            float kq = Math.round(tempRes);
            return kq / 10;
        }

    }

    @Override
    public ExpertResponse getAllExpert(int ofs, int page) throws Exception {
        ExpertResponse response = new ExpertResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());
        MongoCursor<Expert> cursor = collection.find().skip(page*ofs).limit(ofs).as(Expert.class);
        JsonArray jsonArray = new JsonArray();
        response.setTotal(cursor.count());
        while(cursor.hasNext()){
            Expert expert = cursor.next();
            expert.setNameFields(getListNameFieldOfExpert(expert.getIdFields()));
            jsonArray.add(expert.toJson());
        }
        response.setArray(jsonArray);
        return response;
    }

    @Override
    public List<Expert> getListExpertByParentField(String idParentField) throws Exception {
        if (!ObjectId.isValid(idParentField) && !idParentField.equals("")) {
            return null;
        }
        List<Expert> lstExpert = new ArrayList<>();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<Expert> cursor = null;
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());
        builder.append("{$and: [{idParentField: #}]}");
        cursor = collection.find(builder.toString(),idParentField).as(Expert.class);
        while(cursor.hasNext()){
            Expert expert = cursor.next();
            lstExpert.add(expert);
        }
        return lstExpert;
    }

    @Override
    public List<Expert> getExpertByIds(List<String> ids) throws Exception {
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());

        List<ObjectId> lstid = new ArrayList<ObjectId>();
        for (int i=0;i<ids.size();i++){
            lstid.add(new ObjectId(ids.get(i)));
        }
        MongoCursor<Expert> cursor = collection.find("{_id:{$in:#}}", ids).as(Expert.class);
        List<Expert> listExpert = new ArrayList<>();
        while (cursor.hasNext()){
            listExpert.add(cursor.next());
        }
        return listExpert;
    }

    @Override
    public ExpertResponse getListExpertByIdField(String idparentfieldExpert,int ofs, int page) throws Exception {
        ExpertResponse response = new ExpertResponse();
        if (!ObjectId.isValid(idparentfieldExpert)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ idParentField: # }");
        MongoCursor<Expert> cursor = collection.find(stringBuilder.toString(),idparentfieldExpert).skip(page*ofs).limit(ofs).as(Expert.class);
        JsonArray jsonArray = new JsonArray();
        response.setTotal(cursor.count());
        while(cursor.hasNext()){
            Expert expert = cursor.next();
//            expert.setNameFields(getListNameFieldOfExpert(expert.getIdFields()));
            jsonArray.add(expert.toJson());
        }
        response.setArray(jsonArray);
        return response;
    }

    @Override
    public ExpertResponse statiticCommentByExpert(String id) throws Exception {
        ExpertResponse response = new ExpertResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);

        MongoCollection collectionCommentFR = jongo.getCollection(ForumAnswer.class.getSimpleName());

        Calendar calendarST = Calendar.getInstance();
        calendarST.add(Calendar.DATE,-31);
        Calendar calendarED = Calendar.getInstance();
        calendarED.add(Calendar.DATE,-1);
        long startTime = Utils.getStartDay(calendarST.getTimeInMillis());
        long endTime = Utils.getEndDay(calendarED.getTimeInMillis());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{idUser:#,timeCreate:{$gte:#},timeCreate:{$lte:#}}");
        MongoCursor<ForumAnswer> cursor = collectionCommentFR.find(stringBuilder.toString(),id,startTime,endTime).as(ForumAnswer.class);
        JsonObject object = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        List<NumCommentExpert> lstStatitic = initListStatitic(id,startTime,endTime);
        object.addProperty("totalActice",cursor.count());
        while(cursor.hasNext()){
            ForumAnswer forumAnswer = cursor.next();
            for (NumCommentExpert numCommentExpert: lstStatitic){
                if(forumAnswer.getTimeCreate()>=numCommentExpert.getStartTime() && forumAnswer.getTimeCreate()<=numCommentExpert.getEndTime()){
                    int count = numCommentExpert.getCountComment();
                    numCommentExpert.setCountComment(count+1);
                    break;
                }
            }
        }
        for (NumCommentExpert numCommentExpert: lstStatitic){
            jsonArray.add(numCommentExpert.toJson());
        }
        object.add("statitic",jsonArray);
        response.setData(object);
        return response;
    }

    @Override
    public Report gtExpertForDashBoard(Jongo jongo) throws Exception {
        MongoCollection collection = jongo.getCollection(Expert.class.getSimpleName());
        Aggregate.ResultsIterator<ReportObject> cursor = collection.aggregate("{$group: {_id:\"$idParentField\",value:{$sum:1}}}")
                .as(ReportObject.class);
        HashMap<String,ReportObject> hashMap = new HashMap<>();
        int count = 0;
        while(cursor.hasNext()){
            ReportObject reportObject = cursor.next();
            count+=reportObject.getValue();
            hashMap.put(reportObject.get_id(),reportObject);
        }
        Report report = new Report();
        report.setCount(count);
        report.setLst(hashMap);
        return report;
    }

    @Override
    public ExpertResponse searcxhExpert(int ofs, int page, String content) throws Exception {
        return null;
    }


    private List<NumCommentExpert> initListStatitic(String id, long startTime, long endTime){
        List<NumCommentExpert> lstNum = new ArrayList<>();
        while (startTime<endTime){
            long tempTime = startTime+BLOCK_DATE;
            NumCommentExpert numCommentExpert = new NumCommentExpert();
            numCommentExpert.setIdExpert(id);
            numCommentExpert.setStartTime(startTime);
            numCommentExpert.setEndTime(tempTime);
            numCommentExpert.setCountComment(0);
            lstNum.add(numCommentExpert);
            startTime = tempTime;
        }
        return lstNum;
    }

    private JsonArray getListNameFieldOfExpert(List<String> idField){
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(FieldOfExpert.class.getSimpleName());

        List<ObjectId> ids = new ArrayList<ObjectId>();
        for (int i=0;i<idField.size();i++){
            ids.add(new ObjectId(idField.get(i)));
        }
        MongoCursor<FieldOfExpert> cursor = collection.find("{_id:{$in:#}}", ids).as(FieldOfExpert.class);
        JsonArray array = new JsonArray();
        while (cursor.hasNext()){
            array.add(cursor.next().toJsonForExpert());
        }
        return array;
    }
}
