package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

@Data
public class QAAnswer extends MongoLog {
    private String _id;
    private String idQuestion;
    private String content;
    @Override
    public Document toDocument() {
        Document doc = new Document();
        doc.append("_id",new ObjectId(_id));
        doc.append("idQuestion",idQuestion);
        doc.append("content",content);
        return doc;
    }
    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("id",_id);
        object.addProperty("idQuestion",idQuestion);
        object.addProperty("content",content);
        return object;
    }
}
