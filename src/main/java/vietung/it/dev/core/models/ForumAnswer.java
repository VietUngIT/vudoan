package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

@Data
public class ForumAnswer extends MongoLog {
    private String _id;
    private String idQuestion;
    private String phone;
    private String idUser;
    private String avatar;
    private String nameUser;
    private String content;
    private long timeCreate;
    private int numLike;
    private List<String> userLike;
    private Boolean isLiked;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("_id",new ObjectId(_id));
        document.append("idQuestion",idQuestion);
        document.append("idUser",idUser);
        document.append("phone",phone);
        document.append("content",content);
        document.append("numLike",numLike);
        document.append("userLike",userLike);
        document.append("timeCreate",timeCreate);
        return document;
    }
    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("_id",_id);
        object.addProperty("idQuestion",idQuestion);
        object.addProperty("idUser",idUser);
        object.addProperty("phone",phone);
        object.addProperty("avatar",avatar);
        object.addProperty("nameUser",nameUser);
        object.addProperty("numLike",numLike);
        object.addProperty("isLiked",isLiked);
        object.addProperty("timeCreate",timeCreate);
        object.addProperty("content",content);
        return object;
    }
}
