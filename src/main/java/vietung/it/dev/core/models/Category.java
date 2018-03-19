package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

@Data
public class Category extends MongoLog {
    private String _id;
    private String name;
    @Override
    public Document toDocument() {
        Document doc = new Document();
        doc.append("_id",new ObjectId(_id));
        doc.append("name",name);
        return doc;
    }
    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("id",_id);
        object.addProperty("name",name);
        return object;
    }
}
