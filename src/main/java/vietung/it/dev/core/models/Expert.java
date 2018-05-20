package vietung.it.dev.core.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
public class Expert extends MongoLog {
    private String _id;
    private String idUser;
    private String name;
    private String phone;
    private String desc;
    private String email;
    private String address;
    private String workPlace;
    private Double lat = 0D;
    private Double lon = 0D;
    private int numRate;
    private float rate;
    private String idParentField;
    private String nameParentField;
    private List<String> idFields;
    private List<String> tags;
    private List<String> degree;
    private String avatar;
    private Boolean isOnline = false;
    private Double distance = 0D;
    private JsonArray nameFields;
    private int numMatchTags = 0;
    private int numMatchField = 0;
    private int weigthMatch = 0;
    private int numRate1 = 0;
    private int numRate2 = 0;
    private int numRate3 = 0;
    private int numRate4 = 0;
    private int numRate5 = 0;
    private int isRated = 0;
    private float score;
    private List<UserRate> usersRate = new ArrayList<>();

    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("_id",new ObjectId(_id));
        document.append("idUser",idUser);
        document.append("name",name);
        document.append("phone",phone);
        document.append("desc",desc);
        document.append("email",email);
        document.append("address",address);
        document.append("workPlace",workPlace);
        document.append("idParentField",idParentField);
        document.append("tags", tags);
        document.append("lat",lat);
        document.append("lon",lon);
        document.append("numRate",numRate);
        document.append("rate",rate);
        document.append("idFields",idFields);
        document.append("degree",degree);
        document.append("avatar",avatar);
        document.append("isOnline",isOnline);
        document.append("numRate1",numRate1);
        document.append("numRate2",numRate2);
        document.append("numRate3",numRate3);
        document.append("numRate4",numRate4);
        document.append("numRate5",numRate5);
        return document;
    }

    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("_id",_id);
        object.addProperty("name",name);
        object.addProperty("idUser",idUser);
        object.addProperty("phone",phone);
        object.addProperty("desc",desc);
        object.addProperty("email",email);
        object.addProperty("address",address);
        object.addProperty("workPlace",workPlace);
        object.addProperty("idParentField",idParentField);
        object.addProperty("nameParentField",nameParentField);
        List<String> rTags = new ArrayList<>();
        for (int i=0;i<tags.size();i++){
            StringBuilder stringBuilder = new StringBuilder("\"");
            stringBuilder.append(tags.get(i)).append("\"");
            rTags.add(stringBuilder.toString());
        }
        object.addProperty("tags", rTags.toString());
        object.addProperty("numRate",numRate);
        object.addProperty("rate",rate);
        if(nameFields!=null){
            object.add("arrayField",nameFields);
        }
        List<String> rDegree = new ArrayList<>();
        for (int i=0;i<degree.size();i++){
            StringBuilder stringBuilder = new StringBuilder("\"");
            stringBuilder.append(degree.get(i)).append("\"");
            rDegree.add(stringBuilder.toString());
        }
        object.addProperty("degree",rDegree.toString());
        object.addProperty("avatar",avatar);
        object.addProperty("isOnline",isOnline);
        object.addProperty("lat",lat);
        object.addProperty("long",lon);
        object.addProperty("numRate1",numRate1);
        object.addProperty("numRate2",numRate2);
        object.addProperty("numRate3",numRate3);
        object.addProperty("numRate4",numRate4);
        object.addProperty("numRate5",numRate5);
        object.addProperty("isRated",isRated);
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

    public static Comparator<Expert> NUM_MATCH_TAGS_DESC = new Comparator<Expert>() {
        @Override
        public int compare(Expert e1, Expert e2) {
            if (e2.numMatchTags < e1.numMatchTags) {
                return -1;
            } else {
                if (e1.numMatchTags == e2.numMatchTags) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }
    };

    public static Comparator<Expert> NUM_MATCH_FIELD_DESC = new Comparator<Expert>() {
        @Override
        public int compare(Expert e1, Expert e2) {
            if (e2.numMatchField < e1.numMatchField) {
                return -1;
            } else {
                if (e1.numMatchField == e2.numMatchField) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }
    };
    public static Comparator<Expert> NUM_RATE_DESC = new Comparator<Expert>() {
        @Override
        public int compare(Expert e1, Expert e2) {
            if (e2.rate < e1.rate) {
                return -1;
            } else {
                if (e1.rate == e2.rate) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }
    };

    public static Comparator<Expert> WEIGHT_MATCH_DESC = new Comparator<Expert>() {
        @Override
        public int compare(Expert e1, Expert e2) {
            if (e2.weigthMatch < e1.weigthMatch) {
                return -1;
            } else {
                if (e1.weigthMatch == e2.weigthMatch) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }
    };
}
