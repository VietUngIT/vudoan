package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.ForumQuestionResponse;
import vietung.it.dev.core.config.Kafka;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.*;
import vietung.it.dev.core.services.ForumQuestionService;
import vietung.it.dev.core.services.UploadService;
import vietung.it.dev.core.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ForumQuestionServiceImp implements ForumQuestionService {
    @Override
    public ForumQuestionResponse delQuestion(String phone, String id) throws Exception {
        ForumQuestionResponse response = new ForumQuestionResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(ForumQuestion.class.getSimpleName());
        MongoCursor<ForumQuestion> cursor = collection.find("{_id:#}", new ObjectId(id)).as(ForumQuestion.class);
        if(cursor.hasNext()){
            ForumQuestion forumQuestion = cursor.next();
            if(phone.trim().equals(forumQuestion.getPhone())){
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{status:#}}",ForumQuestion.INACTIVE);
                response.setData(forumQuestion.toJson());
            }else {
                response.setError(ErrorCode.LESS_ROLE);
                response.setMsg("Không có quyền xóa.");
            }
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }

        return response;
    }

    @Override
    public ForumQuestionResponse editQuestion(String phone,String id, String content) throws Exception {
        ForumQuestionResponse response = new ForumQuestionResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(ForumQuestion.class.getSimpleName());
        MongoCursor<ForumQuestion> cursor = collection.find("{_id:#}", new ObjectId(id)).as(ForumQuestion.class);
        if(cursor.hasNext()){
            ForumQuestion forumQuestion = cursor.next();
            if(phone.trim().equals(forumQuestion.getPhone())){
                forumQuestion.setContent(content);
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{content:#}}",content);
                response.setData(forumQuestion.toJson());
            }else {
                response.setError(ErrorCode.LESS_ROLE);
                response.setMsg("Không có quyền sửa.");
            }
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }

        return response;
    }

    @Override
    public ForumQuestionResponse likeQuestion(String id, Boolean isLike, String phone) throws Exception {
        ForumQuestionResponse response = new ForumQuestionResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(ForumQuestion.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<ForumQuestion> cursor = collection.find(builder.toString(), new ObjectId(id)).as(ForumQuestion.class);
        if(cursor.hasNext()){
            ForumQuestion forumQuestion = cursor.next();
            int like = forumQuestion.getNumLike()>=0?forumQuestion.getNumLike():0;
            if(isLike){
                forumQuestion.setNumLike(like+1);
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{numLike:#}}",forumQuestion.getNumLike());
                collection.update("{_id:#}", new ObjectId(id)).with("{ $push: {userLike:#}}",phone);
            }else {
                forumQuestion.setNumLike(like-1);
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{numLike:#}}",forumQuestion.getNumLike());
                collection.update("{_id:#}", new ObjectId(id)).with("{ $pull: {userLike:#}}",phone);
            }
            JsonObject object = new JsonObject();
            object.addProperty("numLike",forumQuestion.getNumLike());
            response.setData(object);
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }
        return response;
    }

    @Override
    public int commentQuestion(String id, Boolean isCmt) throws Exception {
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(ForumQuestion.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<ForumQuestion> cursor = collection.find(builder.toString(), new ObjectId(id)).as(ForumQuestion.class);
        if(cursor.hasNext()){
            ForumQuestion forumQuestion = cursor.next();
            int cmt = forumQuestion.getNumComment()>=0?forumQuestion.getNumComment():0;
            if(isCmt){
                forumQuestion.setNumComment(cmt+1);
            }else {
                forumQuestion.setNumComment(cmt-1);
            }

            collection.update("{_id:#}", new ObjectId(id)).with("{$set:{numComment:#}}",forumQuestion.getNumComment());
            return forumQuestion.getNumComment();
        }
        return 0;
    }

    @Override
    public ForumQuestionResponse getQuestionByField(int page, int ofset, String id, String phone) throws Exception {
        ForumQuestionResponse response = new ForumQuestionResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<ForumQuestion> cursor = null;
        MongoCollection collection = jongo.getCollection(ForumQuestion.class.getSimpleName());
        builder.append("{$and: [{idField: #},{status: #}]}");
        cursor = collection.find(builder.toString(),id,ForumQuestion.ACTICE).sort("{timeCreate:-1}").skip(page*ofset).limit(ofset).as(ForumQuestion.class);
        JsonArray jsonArray = new JsonArray();
        response.setTotal(cursor.count());
        while(cursor.hasNext()){
            ForumQuestion forumQuestion = cursor.next();
            Users users = Utils.getUserByPhone(forumQuestion.getPhone());
            if(users!=null){
                forumQuestion.setAvatar(users.getAvatar());
                forumQuestion.setNameUser(users.getName());
            }
            List<String> userLike = forumQuestion.getUserLike();
            if(userLike!=null && userLike.contains(phone)){
                forumQuestion.setIsLiked(true);
            }else{
                forumQuestion.setIsLiked(false);
            }
            jsonArray.add(forumQuestion.toJson());
        }
        response.setArray(jsonArray);

        return response;
    }

    @Override
    public ForumQuestionResponse getQuestionByID(String id, String phone) throws Exception {
        ForumQuestionResponse response = new ForumQuestionResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<ForumQuestion> cursor = null;
        MongoCollection collection = jongo.getCollection(ForumQuestion.class.getSimpleName());
        builder.append("{$and: [{_id: #}]}");
        cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(ForumQuestion.class);
        if(cursor.hasNext()){
            ForumQuestion forumQuestion = cursor.next();
            Users users = Utils.getUserByPhone(forumQuestion.getPhone());
            if(users!=null){
                forumQuestion.setAvatar(users.getAvatar());
                forumQuestion.setNameUser(users.getName());
            }
            List<String> userLike = forumQuestion.getUserLike();
            if(userLike!=null && userLike.contains(phone)){
                forumQuestion.setIsLiked(true);
            }else{
                forumQuestion.setIsLiked(false);
            }
            response.setData(forumQuestion.toJson());
        }else{
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }

        return response;
    }

    @Override
    public ForumQuestionResponse addQuestion(String phone, String image,String idField, String content) throws Exception {

        ForumQuestionResponse response = new ForumQuestionResponse();
        UploadService service = new UploadServiceImp();
        if (!ObjectId.isValid(idField) && !idField.equals("")) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        Users users = Utils.getUserByPhone(phone);
        ForumQuestion forumQuestion = new ForumQuestion();
        ObjectId objectId = new ObjectId();
        String id = objectId.toHexString();
        forumQuestion.set_id(id);
        forumQuestion.setIdField(idField);
        forumQuestion.setIdUser(users.get_id());
        forumQuestion.setPhone(phone);
        forumQuestion.setContent(content);
        forumQuestion.setStatus(ForumQuestion.ACTICE);
        if(image!=null && !image.equals("")){
            try {
                String urlImage = service.uploadImage(image);
                forumQuestion.setImage(urlImage);
            }catch (Exception e){
                response.setError(ErrorCode.UPLOAD_IMAGE_ERROR);
                response.setMsg("Có lỗi trong quá trình upload ảnh.");
                return response;
            }
        }
        forumQuestion.setTimeCreate(Calendar.getInstance().getTimeInMillis());
        forumQuestion.setNumLike(0);
        forumQuestion.setNumComment(0);

        //save data to db
        MongoPool.log(ForumQuestion.class.getSimpleName(),forumQuestion.toDocument());
        //return response to client
        response.setData(forumQuestion.toJson());

        //send content question to sv2 and get tags and field from sv2------------

        //Search Expert
        List<Expert> lstExpert = new ArrayList<>();

        //save data get from sv2 to db
        List<String> lstTag = new ArrayList<>();
        List<String> lstField = new ArrayList<>();
        ExpertRorumQuestion expertRorumQuestion = new ExpertRorumQuestion();
        expertRorumQuestion.set_id(objectId.toHexString());
        expertRorumQuestion.setIdForumQuestion(id);
        expertRorumQuestion.setExperts(lstExpert);
        expertRorumQuestion.setIdField(lstField);
        expertRorumQuestion.setTags(lstTag);
        MongoPool.log(ExpertRorumQuestion.class.getSimpleName(),expertRorumQuestion.toDocument());
//        Kafka.send(forumQuestion.get_id(),forumQuestion.getIdField(),forumQuestion.getContent());
        Kafka.out(forumQuestion.get_id(),forumQuestion.getIdField(),forumQuestion.getContent());
        return response;
    }

    @Override
    public ForumQuestionResponse getExpertByIDQuestion(String id, int numExpert) throws Exception {
        ForumQuestionResponse response = new ForumQuestionResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<ExpertRorumQuestion> cursor = null;
        MongoCollection collection = jongo.getCollection(ExpertRorumQuestion.class.getSimpleName());
        builder.append("{$and: [{idForumQuestion: #}]}");
        cursor = collection.find(builder.toString(),id).limit(1).as(ExpertRorumQuestion.class);
        if(cursor.hasNext()){
            ExpertRorumQuestion expertRorumQuestion = cursor.next();
            List<Expert> lstExpert = expertRorumQuestion.getExperts();
            List<Expert> lstTemp = new ArrayList<>();
            for (int i=0;i<lstExpert.size();i++){
                if(i==numExpert) break;
                lstTemp.add(lstExpert.get(i));
            }
            expertRorumQuestion.setExperts(lstTemp);
            response.setData(expertRorumQuestion.toJsonExpert());
        }else{
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }
        return response;
    }

    @Override
    public ForumQuestionResponse getQuestionAll(int page, int ofset, String phone) throws Exception {
        ForumQuestionResponse response = new ForumQuestionResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<ForumQuestion> cursor = null;
        MongoCollection collection = jongo.getCollection(ForumQuestion.class.getSimpleName());
        builder.append("{$and: [{status: #}]}");
        cursor = collection.find(builder.toString(),ForumQuestion.ACTICE).sort("{timeCreate:-1}").skip(page*ofset).limit(ofset).as(ForumQuestion.class);
        JsonArray jsonArray = new JsonArray();
        response.setTotal(cursor.count());
        while(cursor.hasNext()){
            ForumQuestion forumQuestion = cursor.next();
            Users users = Utils.getUserByPhone(forumQuestion.getPhone());
            if(users!=null){
                forumQuestion.setAvatar(users.getAvatar());
                forumQuestion.setNameUser(users.getName());
            }
            List<String> userLike = forumQuestion.getUserLike();
            if(userLike!=null && userLike.contains(phone)){
                forumQuestion.setIsLiked(true);
            }else{
                forumQuestion.setIsLiked(false);
            }
            jsonArray.add(forumQuestion.toJson());
        }
        response.setArray(jsonArray);

        return response;
    }
}
