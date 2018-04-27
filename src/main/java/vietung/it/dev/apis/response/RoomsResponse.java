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
        jsonObject.addProperty("e", getError());
        jsonObject.addProperty("msg", getMsg());
        jsonObject.addProperty("numByRoom", numByRoom);
        jsonObject.add("data", data);
        jsonObject.add("array", datas);
        return jsonObject.toString();
    }
}
