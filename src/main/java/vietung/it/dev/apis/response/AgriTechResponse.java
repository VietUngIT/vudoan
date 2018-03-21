package vietung.it.dev.apis.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class AgriTechResponse extends BaseResponse {
    private JsonObject data;
    private JsonArray array;
    @Override
    public String toJonString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("e",getError());
        jsonObject.addProperty("msg",getMsg());
        if(data!=null){
            jsonObject.add("data",data);
        }
        if(array!=null){
            jsonObject.add("array",array);
        }
        return jsonObject.toString();
    }
}
