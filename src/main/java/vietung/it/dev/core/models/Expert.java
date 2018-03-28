package vietung.it.dev.core.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Comparator;
import java.util.List;

@Data
public class Expert extends MongoLog {
    private String _id;
    private String name;
    private String phone;
    private String desc;
    private String email;
    private String address;
    private Double lat;
    private Double lon;
    private int numRate;
    private float rate;
    private List<String> idFields;
    private List<String> tags;
    private List<String> degree;
    private String avatar;
    private Boolean isOnline = false;
    private Double distance = 0D;
    private JsonArray nameFields;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("_id",new ObjectId(_id));
        document.append("name",name);
        document.append("phone",phone);
        document.append("desc",desc);
        document.append("email",email);
        document.append("address",address);
        document.append("tags", tags);
        document.append("lat",lat);
        document.append("lon",lon);
        document.append("numRate",numRate);
        document.append("rate",rate);
        document.append("idFields",idFields);
        document.append("degree",degree);
        document.append("avatar",avatar);
        document.append("isOnline",isOnline);
        return document;
    }

    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("_id",_id);
        object.addProperty("name",name);
        object.addProperty("phone",phone);
        object.addProperty("desc",desc);
        object.addProperty("email",email);
        object.addProperty("address",address);
        object.addProperty("tags", tags.toString());
        object.addProperty("numRate",numRate);
        object.addProperty("rate",rate);
        object.addProperty("idFields",idFields.toString());
        if(nameFields!=null){
            object.add("arrayField",nameFields);
        }
        object.addProperty("degree",degree.toString());
        object.addProperty("avatar",avatar);
        object.addProperty("isOnline",isOnline);
        return object;
    }
    public static Comparator<Expert> DISTANCE_ASC = new Comparator<Expert>() {
        @Override
        public int compare(Expert e1, Expert e2) {
            if (e1.distance < e2.distance) {
                return -1;
            } else {
                if (e1.distance == e2.distance) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }
    };
}
