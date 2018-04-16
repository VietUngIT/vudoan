import com.google.gson.JsonArray;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.Variable;
import vietung.it.dev.core.models.Category;
import vietung.it.dev.core.models.Expert;
import vietung.it.dev.core.models.FieldOfExpert;
import vietung.it.dev.core.models.News;
import vietung.it.dev.core.services.ExpertService;
import vietung.it.dev.core.services.FieldOfExpertService;
import vietung.it.dev.core.services.imp.ExpertServiceImp;
import vietung.it.dev.core.services.imp.FieldOfExpertServiceImp;
import vietung.it.dev.core.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MongoTest {
    public static void main(String[] args) {
        try {
            MongoPool.init();
            System.out.println("start");
            FieldOfExpert fieldOfExpert = new FieldOfExpert();
            ObjectId objectId = new ObjectId();
            fieldOfExpert.set_id(objectId.toHexString());
            fieldOfExpert.setNameField("ABC");
            List<String> abc = new ArrayList<>();
            abc.add("aaa");
            fieldOfExpert.setTags(abc);
            MongoPool.log(FieldOfExpert.class.getSimpleName(),fieldOfExpert.toDocument());

//            DB db = MongoPool.getDBJongo();
//            Jongo jongo = new Jongo(db);
//            MongoCollection collection = jongo.getCollection(News.class.getSimpleName());
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append("{ tags: { $elemMatch: { $eq:# } } }");
//            MongoCursor<News> cursor = collection.find(stringBuilder.toString(),"111").as(News.class);
//            while(cursor.hasNext()){
//                News news = cursor.next();
//                System.out.println(news.getTitle());
//            }
            System.out.println("finnish!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }


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
