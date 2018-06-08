package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

@Data
public class Users extends MongoLog {
    public static final int STATUS_ONLINE = 0;
    public static final int STATUS_OFFLINE = 1;

    private String _id;
    private String name;
    private String phone;
    private String password;
    private String address;
    private int roles;
    private String avatar;
    private int status = STATUS_OFFLINE;
    private long createTime;

    @Override
    public Document toDocument() {
        Document doc = new Document();
        doc.append("_id", _id);
        doc.append("name", name);
        doc.append("phone", phone);
        doc.append("password", password);
        doc.append("roles", roles);
        doc.append("address", address);
        doc.append("avatar", avatar);
        doc.append("status", status);
        doc.append("createTime", createTime);
        return doc;
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", _id);
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("phone", phone);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("roles", roles);
        jsonObject.addProperty("address", address);
        jsonObject.addProperty("avatar", avatar);
        jsonObject.addProperty("status", status);
        jsonObject.addProperty("createTime", createTime);
        return jsonObject;
    }
}
