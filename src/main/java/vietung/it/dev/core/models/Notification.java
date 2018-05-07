package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

@Data
public class Notification extends MongoLog {
    public static final int ACTICE = 0;
    public static final int INACTIVE = 1;


    private String _id;
    private String idSend;
    private String nameSend;
    private String avataSend;
    private String idReceiver;
    private String message;
    private String action;
    private int type;
    private int status;
    private long create_at;
    private long update_at;

    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("_id",new ObjectId(_id));
        document.append("idSend",idSend);
        document.append("idReceiver",idReceiver);
        document.append("message",message);
        document.append("action",action);
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
        jsonObject.addProperty("idSend",idSend);
        jsonObject.addProperty("nameSend",nameSend);
        jsonObject.addProperty("avataSend",avataSend);
        jsonObject.addProperty("idReceiver",idReceiver);
        jsonObject.addProperty("message",message);
        jsonObject.addProperty("action",action);
        jsonObject.addProperty("type",type);
        jsonObject.addProperty("status",status);
        jsonObject.addProperty("create_at",create_at);
        jsonObject.addProperty("update_at",update_at);
        return jsonObject;
    }
}
