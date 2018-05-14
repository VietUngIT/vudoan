import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.*;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.Variable;
import vietung.it.dev.core.models.*;
import vietung.it.dev.core.services.*;
import vietung.it.dev.core.services.imp.*;
import vietung.it.dev.core.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MongoTest {
    public static void main(String[] args) {
        try {
            MongoPool.init();
            DB db = MongoPool.getDBJongo();
            Jongo jongo = new Jongo(db);
            System.out.println("start");
            long st = Calendar.getInstance().getTimeInMillis();
            System.out.println(Utils.getUserById("5ae83b16cfd271211434e6cb"));
//            getDistint();
//            testREport();
//            ExpertService service = new ExpertServiceImp();
//            UserService userService = new UserServiceImp();
//            QAQuestionService qaQuestionService = new QAQuestionServiceImp();
//            ForumQuestionService forumQuestionService = new ForumQuestionServiceImp();
//            JsonObject object = service.gtExpertForDashBoard(jongo);
//            JsonObject qaObject = qaQuestionService.getQAForDashBoard(jongo);
//            JsonObject frObject = forumQuestionService.getForumQuestionByDayForDashBoard(jongo);
//
//            System.out.println("result");
//            System.out.println(object);
//            System.out.println("numUser: "+userService.getCountUser(jongo));
//            System.out.println("result");
//            System.out.println(qaObject);
//            System.out.println("result");
//            System.out.println(frObject);
//            List<JsonObject> array = new ArrayList<>();
//            for (int i=0;i<3;i++){
//                ReportObject reportObject = new ReportObject();
//                reportObject.setName("name"+i);
//                reportObject.setValue(i);
//                array.add(reportObject.toJson());
//            }
//            DashBoardObject dashBoardObject = new DashBoardObject();
//            ObjectId id = new ObjectId();
//            dashBoardObject.set_id(id.toHexString());
//            dashBoardObject.setNumExpert(3);
//            dashBoardObject.setExpertByField(array.toString());
//            MongoPool.log(DashBoardObject.class.getSimpleName(),dashBoardObject.toDocument());
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

//            testSearch("nguyên nhân và cách khắc phục");
            long ed = Calendar.getInstance().getTimeInMillis();
            System.out.println(String.valueOf((ed-st)/1000));
            System.out.println("finnish!!!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static void testREport() throws Exception{
        DashBoardServiceImp service = new DashBoardServiceImp();
//        service.getInfo();
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

    private static void getDistint(){
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(ForumAnswer.class.getSimpleName());
        List<String> cursor1 = collection.distinct("idQuestion").query("{\"idUser\":\"5ac1bfeca0e74c0e6c34893c\"}").as(String.class);
        List<ObjectId> lstOBid = new ArrayList<>();
        for (String str: cursor1){
            lstOBid.add(new ObjectId(str));
        }
        MongoCollection collectiontt = jongo.getCollection(ForumQuestion.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:{$in: #}}");
        MongoCursor<ForumQuestion> cursor = collectiontt.find(builder.toString(),lstOBid).as(ForumQuestion.class);
        while(cursor.hasNext()){
            ForumQuestion forumQuestion = cursor.next();
            System.out.println(forumQuestion.get_id()+"-"+forumQuestion.getContent());
        }
//        System.out.println(cursor.get(0));
//        System.out.println("");
    }
}
