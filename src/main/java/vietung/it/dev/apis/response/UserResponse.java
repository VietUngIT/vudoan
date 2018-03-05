package vietung.it.dev.apis.response;

import com.google.gson.JsonObject;
import lombok.Data;
import vietung.it.dev.core.models.Users;

@Data
public class UserResponse extends BaseResponse {
    private Users users;
    @Override
    public String toJonString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("e",getError());
        jsonObject.addProperty("msg",getMsg());
        if(users!=null){
            jsonObject.add("data",users.toJson());
        }
        return jsonObject.toString();
    }
}
