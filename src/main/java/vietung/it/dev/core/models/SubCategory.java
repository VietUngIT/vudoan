package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;


@Data
public class SubCategory extends MongoLog {
    private String _id;
    private String idCate;
    private String nameSubCate;
    private String nameCate;
    @Override
    public Document toDocument() {
        Document doc = new Document();
        doc.append("_id",_id);
        doc.append("idCate",idCate);
        doc.append("nameSubCate",nameSubCate);
        return doc;
    }
    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("id",_id);
        object.addProperty("idCate",idCate);
        object.addProperty("nameSubCate",nameSubCate);
        object.addProperty("nameCate",nameCate);
        return object;
    }
}
