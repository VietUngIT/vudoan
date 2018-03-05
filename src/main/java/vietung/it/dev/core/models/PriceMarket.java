package vietung.it.dev.core.models;

import lombok.Data;
import org.bson.Document;

@Data
public class PriceMarket extends MongoLog {
    private String _id;
    private String namePlace;
    private long price;
    private long timeCreate;
    private int typePM;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("namePlace",namePlace);
        document.append("typePM",typePM);
        document.append("price",price);
        document.append("timeCreate",timeCreate);
        return document;
    }
}
