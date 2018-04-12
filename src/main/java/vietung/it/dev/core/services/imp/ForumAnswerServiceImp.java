package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.ForumAnswerResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.ForumAnswer;
import vietung.it.dev.core.models.Users;
import vietung.it.dev.core.services.ForumAnswerService;
import vietung.it.dev.core.services.ForumQuestionService;
import vietung.it.dev.core.services.UploadService;
import vietung.it.dev.core.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ForumAnswerServiceImp implements ForumAnswerService{
    @Override
    public ForumAnswerResponse getAnswerByQuestion(int page, int ofset, String id, String phone) throws Exception{
        ForumAnswerResponse response = new ForumAnswerResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<ForumAnswer> cursor = null;
        MongoCollection collection = jongo.getCollection(ForumAnswer.class.getSimpleName());
        builder.append("{$and: [{idQuestion: #}]}");
        cursor = collection.find(builder.toString(),id).sort("{timeCreate:-1}").skip(page*ofset).limit(ofset).as(ForumAnswer.class);
        JsonArray jsonArray = new JsonArray();
        response.setTotal(cursor.count());
        while(cursor.hasNext()){
            ForumAnswer forumAnswer = cursor.next();
            Users users = Utils.getUserByPhone(forumAnswer.getPhone());
            if(users!=null){
                forumAnswer.setAvatar(users.getAvatar());
                forumAnswer.setNameUser(users.getName());
            }
            List<String> userLike = forumAnswer.getUserLike();
            if(userLike!=null && userLike.contains(phone)){
                forumAnswer.setIsLiked(true);
            }else{
                forumAnswer.setIsLiked(false);
            }
            jsonArray.add(forumAnswer.toJson());
        }
        response.setArray(jsonArray);

        return response;
    }

    @Override
    public ForumAnswerResponse likeAnswer(String id, Boolean isLike, String phone) throws Exception {
        ForumAnswerResponse response = new ForumAnswerResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(ForumAnswer.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<ForumAnswer> cursor = collection.find(builder.toString(), new ObjectId(id)).as(ForumAnswer.class);
        if(cursor.hasNext()){
            ForumAnswer forumAnswer = cursor.next();
            int like = forumAnswer.getNumLike()>=0?forumAnswer.getNumLike():0;
            if(isLike){
                forumAnswer.setNumLike(like+1);
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{numLike:#}}",forumAnswer.getNumLike());
                collection.update("{_id:#}", new ObjectId(id)).with("{ $push: {userLike:#}}",phone);
            }else {
                forumAnswer.setNumLike(like-1);
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{numLike:#}}",forumAnswer.getNumLike());
                collection.update("{_id:#}", new ObjectId(id)).with("{ $pull: {userLike:#}}",phone);
            }
            JsonObject object = new JsonObject();
            object.addProperty("numLike",forumAnswer.getNumLike());
            response.setData(object);
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }
        return response;
    }

    @Override
    public ForumAnswerResponse delAnswer(String phone, String id) throws Exception {
        ForumAnswerResponse response = new ForumAnswerResponse();
        ForumQuestionService service = new ForumQuestionServiceImp();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(ForumAnswer.class.getSimpleName());
        MongoCursor<ForumAnswer> cursor = collection.find("{_id:#}", new ObjectId(id)).as(ForumAnswer.class);
        if(cursor.hasNext()){
            ForumAnswer forumAnswer = cursor.next();
            if(phone.trim().equals(forumAnswer.getPhone())){
                collection.remove(new ObjectId(id));
                service.commentQuestion(id,false);
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
    public ForumAnswerResponse editAnswer(String phone, String id, String content) throws Exception {
        ForumAnswerResponse response = new ForumAnswerResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(ForumAnswer.class.getSimpleName());
        MongoCursor<ForumAnswer> cursor = collection.find("{_id:#}", new ObjectId(id)).as(ForumAnswer.class);
        if(cursor.hasNext()){
            ForumAnswer forumAnswer = cursor.next();
            if(phone.trim().equals(forumAnswer.getPhone())){
                forumAnswer.setContent(content);
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{content:#}}",content);
                response.setData(forumAnswer.toJson());
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
    public ForumAnswerResponse addAnswer(String id,String phone, String content) throws Exception {
        ForumQuestionService service = new ForumQuestionServiceImp();
        ForumAnswerResponse response = new ForumAnswerResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        Users users = Utils.getUserByPhone(phone);

        ForumAnswer forumAnswer = new ForumAnswer();
        ObjectId _id = new ObjectId();
        forumAnswer.set_id(_id.toHexString());
        forumAnswer.setIdQuestion(id);
        forumAnswer.setPhone(phone);
        forumAnswer.setContent(content);
        forumAnswer.setNumLike(0);
        forumAnswer.setTimeCreate(Calendar.getInstance().getTimeInMillis());
        List<String> userLike = new ArrayList<>();
        forumAnswer.setUserLike(userLike);
        forumAnswer.setIdUser(users.get_id());
        forumAnswer.setAvatar(users.getAvatar());
        forumAnswer.setNameUser(users.getName());
        MongoPool.log(ForumAnswer.class.getSimpleName(),forumAnswer.toDocument());
        response.setData(forumAnswer.toJson());
        service.commentQuestion(id,true);
        return  response;
    }
}
