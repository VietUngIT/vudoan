package vietung.it.dev.apis.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class CategoryResponse extends BaseResponse {
    private JsonObject data;
    private JsonArray array;
    @Override
    public String toJonString() {
        JsonObject object = new JsonObject();
        object.addProperty("e",getError());
        object.addProperty("msg",getMsg());
        if(data!=null){
            object.add("data",data);
        }
        if(array!=null){
            object.add("array",array);
        }
        return object.toString();
    }
}
