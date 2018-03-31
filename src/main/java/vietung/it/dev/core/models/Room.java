package vietung.it.dev.core.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Data
public class Room extends MongoLog{

    public static final int TYPE_ROOM_TWO = 0;
    public static final int TYPE_ROOM_GROUP = 1;
    public static final int STATUS_ROOM_ACTIVATED = 0;
    public static final int STATUS_ROOM_NOT_ACTIVATED = 1;

    private String _id;
    private String name;
    private List<String> user = new ArrayList<>();
    private JsonArray users =  new JsonArray() ;
    private String messages ;
    private long time ;
    private int type;
    private int status;
    private long create_at;
    private long update_at;

    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("_id",new ObjectId(_id));
        document.append("name",name);
        document.append("user",user);
        document.append("type",type);
        document.append("status",status);
        document.append("create_at",create_at);
        document.append("update_at",update_at);
        return document;
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        if(_id!=null){
            jsonObject.addProperty("id",_id);
        }
        jsonObject.addProperty("name",name);
        jsonObject.addProperty("user",user.toString());
        jsonObject.add("users",users);
        jsonObject.addProperty("messages",messages);
        jsonObject.addProperty("time",time);
        jsonObject.addProperty("type",type);
        jsonObject.addProperty("status",status);
        jsonObject.addProperty("create_at",create_at);
        jsonObject.addProperty("update_at",update_at);
        return jsonObject;
    }
}
