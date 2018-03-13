package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

@Data
public class PriceMarket extends MongoLog {
    private String _id;
    private String namePlace;
    private long price;
    private long timeCreate;
    private ObjectId typeNews;
    private String nameTypeNews;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("_id",new ObjectId(_id));
        document.append("namePlace",namePlace);
        document.append("typeNews",typeNews);
        document.append("price",price);
        document.append("timeCreate",timeCreate);
        return document;
    }
    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        if(_id!=null){
            jsonObject.addProperty("id",_id);
        }
        jsonObject.addProperty("namePlace",namePlace);
        jsonObject.addProperty("price",price);
        jsonObject.addProperty("typeNews",String.valueOf(typeNews));
        jsonObject.addProperty("nameTypeNews",nameTypeNews);
        jsonObject.addProperty("timeCreate",timeCreate);
        return jsonObject;
    }
}
