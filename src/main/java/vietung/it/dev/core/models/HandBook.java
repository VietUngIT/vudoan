package vietung.it.dev.core.models;

import com.google.gson.JsonObject;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

@Data
public class HandBook extends MongoLog {
    private String _id;
    private String title;
    private String content;
    private String author;
    private String image;
    private ObjectId typeNews;
    private long timeCreate;
    private String nameTypeNews;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("_id",new ObjectId(_id));
        document.append("title",title);
        document.append("author",author);
        document.append("image",image);
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
        jsonObject.addProperty("author",author);
        jsonObject.addProperty("image",image);
        jsonObject.addProperty("typeNews",String.valueOf(typeNews));
        jsonObject.addProperty("nameTypeNews",nameTypeNews);
        jsonObject.addProperty("timeCreate",timeCreate);
        jsonObject.addProperty("content",content);
        return jsonObject;
    }
}
