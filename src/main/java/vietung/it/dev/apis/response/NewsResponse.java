package vietung.it.dev.apis.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class NewsResponse extends BaseResponse {
    private JsonObject data;
    private JsonArray datas;
    private int numCmtByNew = -1;
    private int total = -1;
    @Override
    public String toJonString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("e",getError());
        jsonObject.addProperty("msg",getMsg());
        jsonObject.addProperty("numCmtByNew",numCmtByNew);
        if(total>-1){
            jsonObject.addProperty("total",total);
        }
        jsonObject.add("data",data);
        jsonObject.add("array",datas);

        return jsonObject.toString();
    }
}
