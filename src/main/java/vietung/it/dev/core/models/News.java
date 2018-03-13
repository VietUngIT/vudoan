package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

@Data
public class News extends MongoLog{
    private String _id;
    private String title;
    private String shortDescription;
    private String content;
    private String author;
    private String image;
    private String source;
    private String tags;
    private int views;
    private int likes;
    private int comments;
    private long timeCreate;
    private ObjectId typeNews;
    private String nameTypeNews;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("_id",new ObjectId(_id));
        document.append("title",title);
        document.append("shortDescription",shortDescription);
        document.append("author",author);
        document.append("image",image);
        document.append("source",source);
        document.append("tags",tags);
        document.append("views",views);
        document.append("likes",likes);
        document.append("comments",comments);
        document.append("typeNews",typeNews);
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
        jsonObject.addProperty("shortDescription",shortDescription);
        jsonObject.addProperty("author",author);
        jsonObject.addProperty("image",image);
        jsonObject.addProperty("source",source);
        jsonObject.addProperty("tags",tags);
        jsonObject.addProperty("views",views);
        jsonObject.addProperty("likes",likes);
        jsonObject.addProperty("comments",comments);
        jsonObject.addProperty("typeNews",String.valueOf(typeNews));
        jsonObject.addProperty("nameTypeNews",nameTypeNews);
        jsonObject.addProperty("timeCreate",timeCreate);
        jsonObject.addProperty("content",content);
        return jsonObject;
    }
}
