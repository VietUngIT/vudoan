package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;
import vietung.it.dev.core.config.MongoPool;

@Data
public class CategoryMarketPrice extends MongoLog {
    private String _id;
    private String name;
    private String image;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("_id",new ObjectId(_id));
        document.append("name",name);
        document.append("image",image);
        return document;
    }

    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("id",_id);
        object.addProperty("name",name);
        object.addProperty("image",image);
        return object;
    }
}
