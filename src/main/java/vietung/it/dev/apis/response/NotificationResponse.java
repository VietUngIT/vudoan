package vietung.it.dev.apis.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;
import vietung.it.dev.core.models.Notification;

@Data
public class NotificationResponse extends BaseResponse {
    private Notification data = null;
    private JsonArray datas;

    @Override
    public String toJonString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("e", getError());
        jsonObject.addProperty("msg", getMsg());
        jsonObject.add("data",data == null ? null: data.toJson());
        jsonObject.add("array", datas);
        return jsonObject.toString();
    }
}
