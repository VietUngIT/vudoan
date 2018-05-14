package vietung.it.dev.core.models;

import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

@Data
public class ExpertQuestionNoti extends MongoLog {
    private String _id;
    private String idExpert;
    private String idQuestion;
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("_id",new ObjectId(_id));
        document.append("idExpert",idExpert);
        document.append("idQuestion",idQuestion);
        return document;
    }
}
