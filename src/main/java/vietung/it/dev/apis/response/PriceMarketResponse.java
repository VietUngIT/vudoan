package vietung.it.dev.apis.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;
import vietung.it.dev.core.models.PriceMarket;

@Data
public class PriceMarketResponse extends BaseResponse {
    private PriceMarket data;
    private JsonArray datas;
    @Override
    public String toJonString() {
        JsonObject object = new JsonObject();
        object.addProperty("e",getError());
        object.addProperty("msg",getMsg());
        if (data!=null){
            object.add("data",data.toJson());
        }
        if(datas!=null){
            object.add("array",datas);
        }
        return object.toString();
    }
}
