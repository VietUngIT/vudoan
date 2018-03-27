package vietung.it.dev.apis.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;
import vietung.it.dev.core.models.Messages;
import vietung.it.dev.core.models.Users;

@Data
public class MessagesResponse extends BaseResponse {
    private JsonObject data;
    private JsonArray datas;
    private int numByMessages = -1;

    @Override
    public String toJonString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("e", getError());
        jsonObject.addProperty("msg", getMsg());
        jsonObject.addProperty("numByMessages", numByMessages);
        jsonObject.add("data", data);
        jsonObject.add("array", datas);
        return jsonObject.toString();
    }
}
