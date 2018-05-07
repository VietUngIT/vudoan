import com.google.gson.JsonArray;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Aggregate;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.Variable;
import vietung.it.dev.core.models.*;
import vietung.it.dev.core.services.ExpertService;
import vietung.it.dev.core.services.FieldOfExpertService;
import vietung.it.dev.core.services.imp.ExpertServiceImp;
import vietung.it.dev.core.services.imp.FieldOfExpertServiceImp;
import vietung.it.dev.core.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MongoTest {
    public static void main(String[] args) {
        try {
            MongoPool.init();
            System.out.println("start");
//            FieldOfExpert fieldOfExpert = new FieldOfExpert();

//            ObjectId objectId = new ObjectId();
//            fieldOfExpert.set_id(objectId.toHexString());
//            fieldOfExpert.setNameField("ABC");
//            List<String> abc = new ArrayList<>();
//            abc.add("aaa");
//            fieldOfExpert.setTags(abc);
//            MongoPool.log(FieldOfExpert.class.getSimpleName(),fieldOfExpert.toDocument());

//            DB db = MongoPool.getDBJongo();
//            Jongo jongo = new Jongo(db);
//            MongoCollection collection = jongo.getCollection(News.class.getSimpleName());
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append("{ tags: { $elemMatch: { $eq:# } } }");
//            MongoCursor<News> cursor = collection.find(stringBuilder.toString(),"111").as(News.class);
//            List<String> list = new ArrayList<>();
//            list.add("blue");
//            list.add("red");
//            list.add("green");
//            list.add("pink");
//            list.add("black");
//            DB db = MongoPool.getDBJongo();
//            Jongo jongo = new Jongo(db);
//            MongoCollection collection = jongo.getCollection(FieldOfExpert.class.getSimpleName());
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append("{ idParentField: {$in:# } }");
//            MongoCursor<FieldOfExpert> cursor = collection.find(stringBuilder.toString(),list).as(FieldOfExpert.class);
//            while(cursor.hasNext()){
//                FieldOfExpert news = cursor.next();
//                System.out.println(news.get_id()+" - "+news.getIdParentField()+" - "+news.getNameField());
//            }
//            FieldOfExpertService service = new FieldOfExpertServiceImp();
//            service.getListFieldMatchTags(list);
            long st = Calendar.getInstance().getTimeInMillis();
            testSearch("nguyên nhân và cách khắc phục");
            long ed = Calendar.getInstance().getTimeInMillis();
            System.out.println(String.valueOf((ed-st)/1000));
            System.out.println("finnish!!!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static void testSearch(String str){
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(QAQuestion.class.getSimpleName());
        collection.ensureIndex("{ content: \"text\",title: \"text\",idField: \"text\"}");
        Aggregate.ResultsIterator<QAQuestion> cursor = collection.aggregate("{\"$match\": { \"$text\":{\"$search\":#},idField: #}}",str,"5ae30e74cfd2712620ab6620")
                .and("{$project:{_id:1,idField:1,title: 1,content: 1,answer:1,score: { $meta: \"textScore\" }}}")
                .and("{$sort:{score: -1}}")
//                .and("{$sort:{num_match_tags:-1}}")
                .as(QAQuestion.class);
//        MongoCursor<QAQuestion> cursor = collection.find("{ $text: { $search: \"Xin hỏi cách chăm sóc cà chua trái vụ\"},score: { $meta: \"textScore\" }}").as(QAQuestion.class);
////        List<String> idFileds = new ArrayList<>();
//        int i = 0;
        while (cursor.hasNext()) {
//            if(i>=5) break;
            QAQuestion qaQuestion = cursor.next();
            System.out.println("----------------------------------");
            System.out.println(qaQuestion.get_id()+"\n"+qaQuestion.getIdField()+"\n"+qaQuestion.getTitle()+"\n"+qaQuestion.getContent()+"\n"+qaQuestion.getScore());
        }
//        collection.
        collection.dropIndex("{ content: \"text\",title: \"text\",idField: \"text\"}");
    }
    private static JsonArray getListNameFieldOfExpert(List<String> idField){
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Variable.MG_CATEGORY_MARKET_INFO);

        List<ObjectId> ids = new ArrayList<ObjectId>();
        for (int i=0;i<idField.size();i++){
            ids.add(new ObjectId(idField.get(i)));
        }
        MongoCursor<Category> cursor = collection.find("{_id:{$in:#}}", ids).as(Category.class);
        JsonArray array = new JsonArray();
        while (cursor.hasNext()){
            array.add(cursor.next().toJson());
        }
        return array;
    }
}
