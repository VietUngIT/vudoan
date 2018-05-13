package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class ReportObject {
    private String _id;
    private String name;
    private int value;

    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("id",_id);
        object.addProperty("name",name);
        object.addProperty("value",value);
        return object;
    }
}
