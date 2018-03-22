package vietung.it.dev.core.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;
import vietung.it.dev.core.utils.Utils;

import java.util.List;

@Data
public class News extends MongoLog{
    private String _id;
    private String idCateNews;
    private String nameCateNews;
    private long timeCreate;
    private String image;
    private String title;
    private String author;
    private String shortDescription;
    private String source;
    private List<String> tags;
    private int numView;
    private int numLike;
    private int numComment;
    private List<String> userLike;
    private String content;
    private Boolean isLiked;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("_id",new ObjectId(_id));
        document.append("title",title);
        document.append("shortDescription",shortDescription);
        document.append("author",author);
        document.append("image",image);
        document.append("source",source);
        document.append("tags", tags);
        document.append("numView",numView);
        document.append("numLike",numLike);
        document.append("numComment",numComment);
        document.append("idCateNews",idCateNews);
        document.append("timeCreate",timeCreate);
        document.append("content",content);
        document.append("userLike", userLike);
        return document;
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        if(_id!=null){
            jsonObject.addProperty("id",_id);
        }
        jsonObject.addProperty("title",title);
        jsonObject.addProperty("shortDescription",shortDescription);
        jsonObject.addProperty("author",author);
        jsonObject.addProperty("image",image);
        jsonObject.addProperty("source",source);
        jsonObject.addProperty("tags",tags.toString());
        jsonObject.addProperty("views",numView);
        jsonObject.addProperty("likes",numLike);
        jsonObject.addProperty("comments",numComment);
        jsonObject.addProperty("idCateNews",idCateNews);
        jsonObject.addProperty("nameCateNews",nameCateNews);
        jsonObject.addProperty("timeCreate",timeCreate);
        jsonObject.addProperty("isLiked",isLiked);
        jsonObject.addProperty("content",content);
        return jsonObject;
    }
}
