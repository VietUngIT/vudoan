package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;

@Data
public class TypeNews extends MongoLog {
    private String _id;
    private int idType;
    private String nameType;
    private boolean isDel = false;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("idType",idType);
        document.append("nameType",nameType);
        document.append("isDel",isDel);
        return document;
    }
    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        if(_id!=null){
            jsonObject.addProperty("id",_id);
        }
        jsonObject.addProperty("idType",idType);
        jsonObject.addProperty("nameType",nameType);
        jsonObject.addProperty("isDel",isDel);
        return jsonObject;
    }
}
