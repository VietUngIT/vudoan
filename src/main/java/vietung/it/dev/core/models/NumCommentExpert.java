package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

@Data
public class NumCommentExpert extends MongoLog {
    private String _id;
    private String idExpert;
    private long startTime;
    private long endTime;
    private int countComment = 0;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("_id",new ObjectId(_id));
        document.append("idExpert",idExpert);
        document.append("startTime",startTime);
        document.append("endTime",endTime);
        document.append("countComment",countComment);
        return document;
    }

    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("id",_id);
        object.addProperty("idExpert",idExpert);
        object.addProperty("startTime",startTime);
        object.addProperty("endTime",endTime);
        object.addProperty("countComment",countComment);
        return object;
    }
}
