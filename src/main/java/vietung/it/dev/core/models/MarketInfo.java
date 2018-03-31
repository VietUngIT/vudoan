package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Data
public class MarketInfo extends MongoLog {
    private String _id;
    private String idCateNews;
    private String nameCateNews;
    private String image;
    private String title;
    private long timeCreate;
    private String author;
    private int numComment;
    private String source;
    private List<String> tags;
    private String content;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("_id",new ObjectId(_id));
        document.append("idCateNews",idCateNews);
        document.append("image",image);
        document.append("title",title);
        document.append("author",author);
        document.append("numComment",numComment);
        document.append("source",source);
        document.append("tags", tags);
        document.append("timeCreate",timeCreate);
        document.append("content",content);
        return document;
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        if(_id!=null){
            jsonObject.addProperty("id",_id);
        }
        jsonObject.addProperty("title",title);
        jsonObject.addProperty("author",author);
        jsonObject.addProperty("image",image);
        jsonObject.addProperty("source",source);
        List<String> rTags = new ArrayList<>();
        for (int i=0;i<tags.size();i++){
            StringBuilder stringBuilder = new StringBuilder("\"");
            stringBuilder.append(tags.get(i)).append("\"");
            rTags.add(stringBuilder.toString());
        }
        jsonObject.addProperty("tags",rTags.toString());
        jsonObject.addProperty("comments",numComment);
        jsonObject.addProperty("idCateNews",idCateNews);
        jsonObject.addProperty("nameCateNews",nameCateNews);
        jsonObject.addProperty("timeCreate",timeCreate);
        jsonObject.addProperty("content",content);
        return jsonObject;
    }
}
