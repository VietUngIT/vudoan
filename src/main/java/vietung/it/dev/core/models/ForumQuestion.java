package vietung.it.dev.core.models;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.cloudinary.json.JSONArray;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class ForumQuestion extends  MongoLog {
    private String _id;
    private String idField;
    private String phone;
    private String idUser;
    private String avatar;
    private String nameUser;
    private String content;
    private List<String> images = new ArrayList<>();
    private List<String> idFields = new ArrayList<>();
    private int numExperts;
    private List<String> idExperts = new ArrayList<>();
    private long timeCreate;
    private int numLike = 0;
    private List<String> userLike = new ArrayList<>();
    private int numComment;
    private Boolean isLiked;

    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("_id",new ObjectId(_id));
        document.append("idField",idField);
        document.append("idFields",idFields);
        document.append("numExperts",numExperts);
        document.append("idExperts",idExperts);
        document.append("idUser",idUser);
        document.append("phone",phone);
        document.append("content",content);
        document.append("images",images);
        document.append("numComment",numComment);
        document.append("numLike",numLike);
        document.append("userLike",userLike);
        document.append("timeCreate",timeCreate);
        return document;
    }
    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("id",_id);
        object.addProperty("idField",idField);
        Gson gson = new Gson();
        JsonElement _idFields = gson.toJsonTree(idFields , new TypeToken<List<String>>() {}.getType());
        object.add("idFields",_idFields.getAsJsonArray());
        object.addProperty("numExperts",numExperts);
        JsonElement _idExperts = gson.toJsonTree(idExperts , new TypeToken<List<String>>() {}.getType());
        object.add("idExperts",_idExperts.getAsJsonArray());
        object.addProperty("phone",phone);
        object.addProperty("avatar",avatar);
        object.addProperty("nameUser",nameUser);
        object.addProperty("idUser",idUser);
        object.addProperty("numComment",numComment);
        object.addProperty("numLike",numLike);
        object.addProperty("isLiked",isLiked);
        object.addProperty("timeCreate",timeCreate);
        object.addProperty("content",content);
        JsonElement _images = gson.toJsonTree(images , new TypeToken<List<String>>() {}.getType());
        object.add("images",_images.getAsJsonArray());
        return object;
    }
}
