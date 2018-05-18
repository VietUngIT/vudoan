package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;

@Data
public class UserRate extends MongoLog {
    private String user;
    private int rate;

    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("user",user);
        object.addProperty("rate",rate);
        return object;
    }

    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("user",user);
        document.append("rate",rate);
        return document;
    }
}
