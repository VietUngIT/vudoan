package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@Data
public class FieldOfExpert extends MongoLog {
    private String _id;
    private int idField;
    private String nameField;
    private List<String> tags = new ArrayList<>();
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("idField",idField);
        document.append("nameField",nameField);
        document.append("tags",tags);
        return document;
    }

    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("idField",idField);
        object.addProperty("nameField",nameField);
        List<String> rTags = new ArrayList<>();
        for (int i=0;i<tags.size();i++){
            StringBuilder stringBuilder = new StringBuilder("\"");
            stringBuilder.append(tags.get(i)).append("\"");
            rTags.add(stringBuilder.toString());
        }
        object.addProperty("tags",rTags.toString());
        return object;
    }
}
