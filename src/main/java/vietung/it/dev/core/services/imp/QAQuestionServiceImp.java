package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Aggregate;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.QAQuestionResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.QAQuestion;
import vietung.it.dev.core.models.Report;
import vietung.it.dev.core.models.ReportObject;
import vietung.it.dev.core.services.QAQuestionService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
    public QAQuestionResponse editQAQuestion(String id,String title, String content,String answer) throws Exception {
        QAQuestionResponse response = new QAQuestionResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(QAQuestion.class.getSimpleName());
        MongoCursor<QAQuestion> cursor = collection.find("{_id:#}", new ObjectId(id)).limit(1).as(QAQuestion.class);
        if(cursor.hasNext()){
            QAQuestion qaQuestion = cursor.next();
            collection.update("{_id:#}", new ObjectId(id)).with("{$set:{title:#,content:#,answer:#}}",title,content,answer);
            qaQuestion.setTitle(title);
            qaQuestion.setContent(content);
            qaQuestion.setAnswer(answer);
            response.setData(qaQuestion.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }
        return response;
    }

    @Override
    public QAQuestionResponse addQAQuestion(String idfield,String title, String content,String answer) throws Exception {
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
        qaQuestion.setTitle(title);
        qaQuestion.setAnswer(answer);
        response.setData(qaQuestion.toJson());
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

    @Override
    public QAQuestionResponse searchQA(String content, String id) throws Exception {
        long st = Calendar.getInstance().getTimeInMillis();
        QAQuestionResponse response = new QAQuestionResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(QAQuestion.class.getSimpleName());
        collection.ensureIndex("{ content: \"text\",title: \"text\",idField: \"text\"}");
        Aggregate.ResultsIterator<QAQuestion> cursor = collection.aggregate("{\"$match\": { \"$text\":{\"$search\":#},idField: #}}",content,id)
                .and("{$project:{_id:1,idField:1,title: 1,content: 1,answer:1,score: { $meta: \"textScore\" }}}")
                .and("{$sort:{score: -1}}")
                .as(QAQuestion.class);
        JsonArray array = new JsonArray();
        int i = 0;
        while (cursor.hasNext()) {
            if(i>=5) break;
            QAQuestion qaQuestion = cursor.next();
            array.add(qaQuestion.toJson());
        }
        collection.dropIndex("{ content: \"text\",title: \"text\",idField: \"text\"}");
        response.setArray(array);
        long ed = Calendar.getInstance().getTimeInMillis();
        System.out.println(String.valueOf((ed-st)/1000));
        return response;
    }

    @Override
    public Report getQAForDashBoard(Jongo jongo) throws Exception {
        MongoCollection collection = jongo.getCollection(QAQuestion.class.getSimpleName());
        Aggregate.ResultsIterator<ReportObject> cursor = collection.aggregate("{$group: {_id:\"$idField\",value:{$sum:1}}}")
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
}
