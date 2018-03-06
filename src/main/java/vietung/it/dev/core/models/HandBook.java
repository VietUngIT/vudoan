package vietung.it.dev.core.models;

import lombok.Data;
import org.bson.Document;

@Data
public class HandBook extends MongoLog {
    private String _id;
    private String title;
    private String content;
    private String author;
    private String image;
    private int views;
    private String typeNews;
    private long timeCreate;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("title",title);
        document.append("author",author);
        document.append("image",image);
        document.append("views",views);
        document.append("typeNews",typeNews);
        document.append("timeCreate",timeCreate);
        document.append("content",content);
        return document;
    }
}
