package vietung.it.dev.apis.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;
import vietung.it.dev.core.models.FieldOfExpert;

@Data
public class FieldOfExpertResponse extends BaseResponse {
    private JsonObject data;
    private JsonArray array;
    @Override
    public String toJonString() {
        JsonObject object = new JsonObject();
        object.addProperty("e",getError());
        object.addProperty("msg",getMsg());
        object.add("data",data);
        object.add("array",array);
        return object.toString();
    }
}
