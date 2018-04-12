package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.ForumQuestionResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.ForumQuestion;
import vietung.it.dev.core.models.Messages;
import vietung.it.dev.core.models.Users;
import vietung.it.dev.core.services.ForumQuestionService;
import vietung.it.dev.core.services.UploadService;
import vietung.it.dev.core.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ForumQuestionServiceImp implements ForumQuestionService {
    @Override
    public ForumQuestionResponse addQuestion(String idUser, String phone, String idField, int numExperts, String content, JsonArray images) throws Exception {
        ForumQuestionResponse response = new ForumQuestionResponse();
        if (!ObjectId.isValid(idUser)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        if(idField!= null && !ObjectId.isValid(idField)){
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        UploadService service = new UploadServiceImp();
        List<String> urlImages = new ArrayList<>();
        for(int i = 0 ; i < images.size() ; i++){
            try {
                String urlImage = service.uploadImage(images.get(i).getAsString());
                urlImages.add(urlImage);
            } catch (IOException e) {
                response.setError(ErrorCode.UPLOAD_IMAGE_ERROR);
                return response;
            }
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(ForumQuestion.class.getSimpleName());
        Calendar calendar = Calendar.getInstance();
        ForumQuestion forumQuestion = new ForumQuestion();
        ObjectId id = new ObjectId();
        forumQuestion.set_id(id.toHexString());
        forumQuestion.setIdUser(idUser);
        forumQuestion.setPhone(phone);
        forumQuestion.setIdField(idField);
        forumQuestion.setNumExperts(numExperts);
        forumQuestion.setContent(content);
        forumQuestion.setImages(urlImages);
        forumQuestion.setTimeCreate(calendar.getTimeInMillis());
        MongoPool.log(ForumQuestion.class.getSimpleName(), forumQuestion.toDocument());
        response.setData(forumQuestion.toJson());
        return response;
    }

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
                collection.remove(new ObjectId(id));
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
        builder.append("{$and: [{idField: #}]}");
        cursor = collection.find(builder.toString(),id).sort("{timeCreate:-1}").skip(page*ofset).limit(ofset).as(ForumQuestion.class);
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
    public ForumQuestionResponse getQuestionAll(int page, int ofset,String phone) throws Exception {
        ForumQuestionResponse response = new ForumQuestionResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<ForumQuestion> cursor = null;
        MongoCollection collection = jongo.getCollection(ForumQuestion.class.getSimpleName());
        cursor = collection.find().sort("{timeCreate:-1}").skip(page).limit(ofset).as(ForumQuestion.class);
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
