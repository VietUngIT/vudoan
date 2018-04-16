package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

@Data
public class QAQuestion extends MongoLog {
    private String _id;
    private String idField;
    private String content;
    @Override
    public Document toDocument() {
        Document doc = new Document();
        doc.append("_id",new ObjectId(_id));
        doc.append("idField",idField);
        doc.append("content",content);
        return doc;
    }
    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("id",_id);
        object.addProperty("idField",idField);
        object.addProperty("content",content);
        return object;
    }
}
