package vietung.it.dev.apis.response;

import com.google.gson.JsonObject;

public class SimpleResponse extends BaseResponse {
    @Override
    public String toJonString() {
        JsonObject object = new JsonObject();
        object.addProperty("e",getError());
        object.addProperty("msg",getMsg());
        return object.toString();
    }
}
