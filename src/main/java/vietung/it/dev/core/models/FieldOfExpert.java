package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;

@Data
public class FieldOfExpert extends MongoLog {
    private String _id;
    private int idField;
    private String nameField;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("idField",idField);
        document.append("nameField",nameField);
        return document;
    }

    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("idField",idField);
        object.addProperty("nameField",nameField);
        return object;
    }
}
