package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.QAAnswerResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.QAAnswer;
import vietung.it.dev.core.services.QAAnswerService;

public class QAAnswerServiceImp implements QAAnswerService {
    @Override
    public QAAnswerResponse delQAAnswer(String id) throws Exception {
        QAAnswerResponse response = new QAAnswerResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(QAAnswer.class.getSimpleName());
        MongoCursor<QAAnswer> cursor = collection.find("{_id:#}", new ObjectId(id)).as(QAAnswer.class);
        if(cursor.hasNext()){
            collection.remove(new ObjectId(id));
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }
        return response;
    }

    @Override
    public QAAnswerResponse editQAAnswer(String id, String idQuestion, String content) throws Exception {
        QAAnswerResponse response = new QAAnswerResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        if (!ObjectId.isValid(idQuestion)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id field không đúng.");
            return response;
        }

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(QAAnswer.class.getSimpleName());
        MongoCursor<QAAnswer> cursor = collection.find("{_id:#}", new ObjectId(id)).limit(1).as(QAAnswer.class);
        if(cursor.hasNext()){
            QAAnswer qaAnswer = cursor.next();
            collection.update("{_id:#}", new ObjectId(id)).with("{$set:{idQuestion:#,content:#}}",idQuestion,content);
            response.setData(qaAnswer.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }
        return response;
    }

    @Override
    public QAAnswerResponse addQAAnswer(String idQuestion, String content) throws Exception {
        QAAnswerResponse response = new QAAnswerResponse();
        if (!ObjectId.isValid(idQuestion)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id field không đúng.");
            return response;
        }
        QAAnswer qaAnswer = new QAAnswer();
        ObjectId _id = new ObjectId();
        qaAnswer.set_id(_id.toHexString());
        qaAnswer.setIdQuestion(idQuestion);
        qaAnswer.setContent(content);
        MongoPool.log(QAAnswer.class.getSimpleName(),qaAnswer.toDocument());
        return  response;
    }

    @Override
    public QAAnswerResponse getQAAnswerByIDQuestion(String idQuestion, int page, int ofset) throws Exception {
        QAAnswerResponse response = new QAAnswerResponse();
        if (!ObjectId.isValid(idQuestion)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<QAAnswer> cursor = null;
        MongoCollection collection = jongo.getCollection(QAAnswer.class.getSimpleName());
        builder.append("{$and: [{idQuestion: #}]}");
        cursor = collection.find(builder.toString(),idQuestion).skip(page*ofset).limit(ofset).as(QAAnswer.class);
        JsonArray jsonArray = new JsonArray();
        response.setTotal(cursor.count());
        while(cursor.hasNext()){
            QAAnswer qaAnswer = cursor.next();
            jsonArray.add(qaAnswer.toJson());
        }
        response.setArray(jsonArray);
        return response;
    }
}
