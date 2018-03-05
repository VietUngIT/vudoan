package vietung.it.dev.core.models;

import lombok.Data;
import org.bson.Document;

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
    private int comments;
    private long timeCreate;
    private String typeNews;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("title",title);
        document.append("shortDescription",shortDescription);
        document.append("content",content);
        document.append("author",author);
        document.append("image",image);
        document.append("source",source);
        document.append("tags",tags);
        document.append("views",views);
        document.append("comments",comments);
        document.append("typeNews",typeNews);
        document.append("timeCreate",timeCreate);
        return document;
    }
}
