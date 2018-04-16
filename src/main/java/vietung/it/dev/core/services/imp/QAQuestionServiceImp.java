package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.QAQuestionResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.QAQuestion;
import vietung.it.dev.core.services.QAQuestionService;

public class QAQuestionServiceImp implements QAQuestionService {
    @Override
    public QAQuestionResponse delQAQuestion(String id) throws Exception {
        QAQuestionResponse response = new QAQuestionResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(QAQuestion.class.getSimpleName());
        MongoCursor<QAQuestion> cursor = collection.find("{_id:#}", new ObjectId(id)).as(QAQuestion.class);
        if(cursor.hasNext()){
            collection.remove(new ObjectId(id));
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }
        return response;
    }

    @Override
    public QAQuestionResponse editQAQuestion(String id, String idfield, String content) throws Exception {
        QAQuestionResponse response = new QAQuestionResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        if (!ObjectId.isValid(idfield)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id field không đúng.");
            return response;
        }

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(QAQuestion.class.getSimpleName());
        MongoCursor<QAQuestion> cursor = collection.find("{_id:#}", new ObjectId(id)).limit(1).as(QAQuestion.class);
        if(cursor.hasNext()){
            QAQuestion qaQuestion = cursor.next();
            collection.update("{_id:#}", new ObjectId(id)).with("{$set:{idField:#,content:#}}",idfield,content);
            response.setData(qaQuestion.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }
        return response;
    }

    @Override
    public QAQuestionResponse addQAQuestion(String idfield, String content) throws Exception {
        QAQuestionResponse response = new QAQuestionResponse();
        if (!ObjectId.isValid(idfield)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id field không đúng.");
            return response;
        }
        QAQuestion qaQuestion = new QAQuestion();
        ObjectId _id = new ObjectId();
        qaQuestion.set_id(_id.toHexString());
        qaQuestion.setIdField(idfield);
        qaQuestion.setContent(content);
        MongoPool.log(QAQuestion.class.getSimpleName(),qaQuestion.toDocument());
        return  response;
    }

    @Override
    public QAQuestionResponse getQAQueationByIdField(int page, int ofset, String idfield) throws Exception {
        QAQuestionResponse response = new QAQuestionResponse();
        if (!ObjectId.isValid(idfield)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<QAQuestion> cursor = null;
        MongoCollection collection = jongo.getCollection(QAQuestion.class.getSimpleName());
        builder.append("{$and: [{idField: #}]}");
        cursor = collection.find(builder.toString(),idfield).skip(page*ofset).limit(ofset).as(QAQuestion.class);
        JsonArray jsonArray = new JsonArray();
        response.setTotal(cursor.count());
        while(cursor.hasNext()){
            QAQuestion qaQuestion = cursor.next();
            jsonArray.add(qaQuestion.toJson());
        }
        response.setArray(jsonArray);
        return response;
    }
}
