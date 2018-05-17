package vietung.it.dev.apis.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class CommentResponse extends BaseResponse {
    private JsonObject data;
    private JsonArray array;
    private int total = -1;
    @Override
    public String toJonString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("e",getError());
        jsonObject.addProperty("msg",getMsg());
        if(total>-1){
            jsonObject.addProperty("total",total);
        }
        jsonObject.add("data",data);
        jsonObject.add("array",array);

        return jsonObject.toString();
    }
}
