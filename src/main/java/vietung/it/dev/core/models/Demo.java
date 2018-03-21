package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;

@Data
public class Demo extends MongoLog {
    private String _id;
    private String value;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("value",value);
        return document;
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_id",_id);
        jsonObject.addProperty("value",value);
        return jsonObject;
    }
}
