package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.TypeNewsResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.TypeNews;
import vietung.it.dev.core.services.TypeNewsService;

public class TypeNewsServiceImp implements TypeNewsService {
    @Override
    public TypeNewsResponse getTypeNews(String id) {
        TypeNewsResponse response = new TypeNewsResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(TypeNews.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<TypeNews> cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(TypeNews.class);
        if(cursor.hasNext()){
            TypeNews typeNews = cursor.next();
            response.setTypeNews(typeNews);
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }
        return response;
    }

    @Override
    public TypeNewsResponse getAllTypeNews(int typeCate) {
        TypeNewsResponse response = new TypeNewsResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(TypeNews.class.getSimpleName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{typeCate:#}");
        MongoCursor<TypeNews> cursor = collection.find(stringBuilder.toString(),typeCate).as(TypeNews.class);
        JsonArray jsonArray = new JsonArray();
        while (cursor.hasNext()){
            TypeNews typeNews = cursor.next();
            jsonArray.add(typeNews.toJson());
        }
        response.setArrTypeNews(jsonArray);
        return response;
    }

    @Override
    public TypeNewsResponse addTypeNews(String nameType,int typeCate) {
        TypeNewsResponse response = new TypeNewsResponse();
        TypeNews typeNews = new TypeNews();
        ObjectId _id = new ObjectId();
        typeNews.set_id(_id.toHexString());
        typeNews.setTypeCate(typeCate);
        typeNews.setNameType(nameType);
        MongoPool.log(TypeNews.class.getSimpleName(),typeNews.toDocument());
        response.setTypeNews(typeNews);

        return response;
    }

    @Override
    public TypeNewsResponse editTypeNews(String id, String nameType) {
        TypeNewsResponse response = new TypeNewsResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(TypeNews.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<TypeNews> cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(TypeNews.class);
        if(cursor.hasNext()){
            TypeNews typeNews = cursor.next();
            typeNews.setNameType(nameType);
            collection.update("{_id:#}", new ObjectId(id)).with("{$set:{nameType:#}}",nameType);
            response.setTypeNews(typeNews);
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
            return response;
        }
        return response;
    }

//    class UpdateNameTypeNewsRunnable implements Runnable {
//        private Thread t;
//        public void run() {
//            try {
//                updateNameTypeNews();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        public void start (){
//            if (t == null){
//                t = new Thread (this);
//                t.start ();
//            }
//        }
//    }
//
//    private void updateNameTypeNews() {
//    }

}
