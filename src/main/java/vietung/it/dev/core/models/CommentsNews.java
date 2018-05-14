package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

@Data
public class CommentsNews extends MongoLog {
    private String _id;
    private String idUser;
    private String name;
    private String phone;
    private String avatar;
    private String content;
    private String idNews;
    private long timeCreate;

    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("_id",new ObjectId(_id));
        document.append("phone",phone);
        document.append("idNews",idNews);
        document.append("timeCreate",timeCreate);
        document.append("content",content);
        document.append("idUser",idUser);
        return document;
    }
    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        if(_id!=null){
            jsonObject.addProperty("_id",_id);
        }
        jsonObject.addProperty("name",name);
        jsonObject.addProperty("avatar",avatar);
        jsonObject.addProperty("phone",phone);
        jsonObject.addProperty("idNews",idNews);
        jsonObject.addProperty("timeCreate",timeCreate);
        jsonObject.addProperty("content",content);
        jsonObject.addProperty("idUser",idUser);
        return jsonObject;
    }
}
