package vietung.it.dev.core.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

@Data
public class ExpertRorumQuestion extends MongoLog{
    private String _id;
    private String idForumQuestion;
    private List<String> tags;
    private List<String> idField;
    private List<String> idExpert;
    private List<Expert> experts;

    @Override
    public Document toDocument() {
        Document doc = new Document();
        doc.append("_id", new ObjectId(_id));
        doc.append("idForumQuestion",idForumQuestion);
        doc.append("tags",tags);
        doc.append("idField",idField);
        doc.append("idExpert",idExpert);
        doc.append("experts",experts);
        return doc;
    }

    public JsonObject toJsonExpert(){
        JsonObject object = new JsonObject();
        object.addProperty("id",_id);
        object.addProperty("idForumQuestion",idForumQuestion);
        JsonArray array = new JsonArray();
        for (int i=0;i<experts.size();i++){
            Expert expert = experts.get(i);
            array.add(expert.toJson());
        }
        object.add("array",array);
        return object;
    }
}
