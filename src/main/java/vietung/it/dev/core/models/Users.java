package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;

@Data
public class Users extends MongoLog {
    private String _id;
    private String name;
    private String phone;
    private String password;
    private String address;
    private int roles;
    private int avatar;
    private long createTime;

    @Override
    public Document toDocument() {
        Document doc = new Document();
        doc.append("name", name);
        doc.append("phone", phone);
        doc.append("password", password);
        doc.append("roles", roles);
        doc.append("address", address);
        doc.append("avatar", avatar);
        doc.append("createTime", createTime);
        return doc;
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("phone", phone);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("roles", roles);
        jsonObject.addProperty("address", address);
        jsonObject.addProperty("avatar", avatar);
        jsonObject.addProperty("createTime", createTime);
        return jsonObject;
    }
}
