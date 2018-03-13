package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

@Data
public class TypeNews extends MongoLog {
    private String _id;
    private int typeCate;
    private String nameType;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("_id",new ObjectId(_id));
        document.append("typeCate",typeCate);
        document.append("nameType",nameType);
        return document;
    }
    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        if(_id!=null){
            jsonObject.addProperty("id",_id);
        }
        jsonObject.addProperty("nameType",nameType);
        jsonObject.addProperty("typeCate",typeCate);
        return jsonObject;
    }
}
