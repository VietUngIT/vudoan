package vietung.it.dev.apis.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;
import vietung.it.dev.core.models.FieldOfExpert;

@Data
public class FieldOfExpertResponse extends BaseResponse {
    private FieldOfExpert fieldOfExpert;
    private JsonArray listFieldOfExpert;
    @Override
    public String toJonString() {
        JsonObject object = new JsonObject();
        object.addProperty("e",getError());
        object.addProperty("msg",getMsg());
        if(fieldOfExpert!=null){
            object.add("data",fieldOfExpert.toJson());
        }
        if(listFieldOfExpert!=null){
            object.add("array",listFieldOfExpert);
        }
        return object.toString();
    }
}
