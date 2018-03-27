package vietung.it.dev.apis.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class RoomsResponse extends BaseResponse {
    private JsonObject data;
    private JsonArray datas;
    private int numByRoom = -1;
    @Override
    public String toJonString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("e",getError());
        jsonObject.addProperty("msg",getMsg());
        if (numByRoom!=-1){
            jsonObject.addProperty("numByRoom",numByRoom);
        }
        if(data!=null){
            jsonObject.add("data",data);
        }
        if(datas!=null){
            jsonObject.add("array",datas);
        }
        return jsonObject.toString();
    }
}
