package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

@Data
public class DashBoardObject extends MongoLog {
    private String _id;
    private int numExpert;
    private String expertByField ;
    private int numUser;
    private int numQA;
    private String qaByField ;
    private String forumByDay ;
    private int numQuestionFR;
    private String frByField;
    private long startTime;
    private long endTime;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("_id",new ObjectId(_id));
        document.append("numExpert",numExpert);
        document.append("expertByField",expertByField);
        document.append("numUser",numUser);
        document.append("numQA",numQA);
        document.append("qaByField",qaByField);
        document.append("forumByDay",forumByDay);
        document.append("numQuestionFR",numQuestionFR);
        document.append("frByField",frByField);
        document.append("startTime",startTime);
        document.append("endTime",endTime);
        return document;
    }

    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("numExpert",numExpert);
        object.addProperty("expertByField",expertByField);
        object.addProperty("numUser",numUser);
        object.addProperty("numQA",numQA);
        object.addProperty("qaByField",qaByField);
        object.addProperty("forumByDay",forumByDay);
        object.addProperty("numQuestionFR",numQuestionFR);
        object.addProperty("frByField",frByField);
        object.addProperty("startTime",startTime);
        object.addProperty("endTime",endTime);
        return object;
    }
}
