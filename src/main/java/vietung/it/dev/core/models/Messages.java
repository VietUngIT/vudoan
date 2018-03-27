package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

@Data
public class Messages extends MongoLog{
    public static final int TYPE_MESSAGE_TEXT = 0;
    public static final int TYPE_MESSAGE_IMAGE = 1;
    public static final int TYPE_MESSAGE_MICRO = 2;
    public static final int TYPE_MESSAGE_LIKE = 3;

    private String _id;
    private String idRoom;
    private String idUser;
    private String message;
    private int type;
    private int status;
    private long create_at;
    private long update_at;

    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("idRoom",idRoom);
        document.append("idUser",idUser);
        document.append("message",message);
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
        jsonObject.addProperty("idRoom",idRoom);
        jsonObject.addProperty("idUser",idUser);
        jsonObject.addProperty("message",message);
        jsonObject.addProperty("type",type);
        jsonObject.addProperty("status",status);
        jsonObject.addProperty("create_at",create_at);
        jsonObject.addProperty("update_at",update_at);
        return jsonObject;
    }
}
