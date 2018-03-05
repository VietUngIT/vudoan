package vietung.it.dev.core.models;

import lombok.Data;
import org.bson.Document;

@Data
public class CommentsNews extends MongoLog {
    private String _id;
    private String name;
    private String phone;
    private int avatar;
    private String content;
    private String idNews;
    private long timeCreate;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("name",name);
        document.append("avatar",avatar);
        document.append("phone",phone);
        document.append("content",content);
        document.append("idNews",idNews);
        document.append("timeCreate",timeCreate);
        return document;
    }
}
