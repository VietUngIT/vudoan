package vietung.it.dev.apis.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;
import vietung.it.dev.core.models.TypeNews;
import vietung.it.dev.core.utils.Utils;

@Data
public class TypeNewsResponse extends BaseResponse {
    private TypeNews typeNews;
    private JsonArray arrTypeNews;
    @Override
    public String toJonString() {
        JsonObject object = new JsonObject();
        object.addProperty("e",getError());
        object.addProperty("msg",getMsg());
        object.add("data", typeNews.toJson());
        object.add("array",arrTypeNews);
        return object.toString();
    }
}
