package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

@Data
public class MarketPrice extends MongoLog {
    private String _id;
    private String idCate;
    private String nameCate;
    private String name;
    private String place;
    private long price;
    private long timeCreate;
    private String unit;
    private String note;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("_id",new ObjectId(_id));
        document.append("idCate",idCate);
        document.append("name",name);
        document.append("place",place);
        document.append("price",price);
        document.append("unit",unit);
        document.append("timeCreate",timeCreate);
        document.append("note",note);
        return document;
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        if(_id!=null){
            jsonObject.addProperty("id",_id);
        }
        jsonObject.addProperty("idCate",idCate);
        jsonObject.addProperty("name",name);
        jsonObject.addProperty("place",place);
        jsonObject.addProperty("price",price);
        jsonObject.addProperty("unit",unit);
        jsonObject.addProperty("timeCreate",timeCreate);
        jsonObject.addProperty("note",note);
        return jsonObject;
    }
}
