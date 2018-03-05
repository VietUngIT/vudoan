package vietung.it.dev.core.models;

import lombok.Data;
import org.bson.Document;

@Data
public class TypePriceMarket extends MongoLog {
    private String _id;
    private int typePM;
    private String name;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("typePM",typePM);
        document.append("name",name);
        return document;
    }
}
