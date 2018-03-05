package vietung.it.dev.core.models;

import lombok.Data;
import org.bson.Document;

@Data
public class TypeNews extends MongoLog {
    private String _id;
    private int idType;
    private String nameType;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("idType",idType);
        document.append("nameType",nameType);
        return document;
    }
}
