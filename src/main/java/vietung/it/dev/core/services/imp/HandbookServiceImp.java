package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.HandBookResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.HandBook;
import vietung.it.dev.core.models.TypeNews;
import vietung.it.dev.core.services.HandbookService;

import java.util.Calendar;

public class HandbookServiceImp implements HandbookService{

    @Override
    public HandBookResponse getAllHandbook(long timelast, int offer) {
        HandBookResponse response = new HandBookResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCollection collection = jongo.getCollection(HandBook.class.getSimpleName());
        MongoCursor<HandBook> cursor;
        if(timelast>0){
            builder.append("{timeCreate:{$lt:#}}");
            cursor = collection.find(builder.toString(),timelast).sort("{timeCreate:-1}").limit(offer).as(HandBook.class);
        }else {
            cursor = collection.find().sort("{timeCreate:-1}").limit(offer).as(HandBook.class);
        }

        MongoCollection collectionType = jongo.getCollection(TypeNews.class.getSimpleName());
        JsonArray jsonArray = new JsonArray();
        while(cursor.hasNext()){
            HandBook handBook = cursor.next();
            MongoCursor<TypeNews> cursorType = collectionType.find(builder.toString(), handBook.getTypeNews()).as(TypeNews.class);
            if(cursorType.hasNext()){
                handBook.setNameTypeNews(cursorType.next().getNameType());
            }
            jsonArray.add(handBook.toJson());
        }
        response.setDatas(jsonArray);

        return response;
    }

    @Override
    public HandBookResponse getAllHandbookByType(long timelast, int offer, String idtype) {
        HandBookResponse response = new HandBookResponse();
        if (!ObjectId.isValid(idtype)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<HandBook> cursor = null;
        MongoCollection collection = jongo.getCollection(HandBook.class.getSimpleName());
        if(timelast>0){
            builder.append("{$and: [{typeNews: #},{timeCreate:{$lt:#}}]}");
            cursor = collection.find(builder.toString(),new ObjectId(idtype),timelast).sort("{timeCreate:-1}").limit(offer).as(HandBook.class);
        }else {
            builder.append("{$and: [{typeNews: #}]}");
            cursor = collection.find(builder.toString(),new ObjectId(idtype)).sort("{timeCreate:-1}").limit(offer).as(HandBook.class);
        }
        MongoCollection collectionType = jongo.getCollection(TypeNews.class.getSimpleName());
        MongoCursor<TypeNews> cursorType = collectionType.find(builder.toString(), new ObjectId(idtype)).as(TypeNews.class);
        String nameType = "";
        if(cursorType.hasNext()){
            nameType = cursorType.next().getNameType();
        }
        JsonArray jsonArray = new JsonArray();
        while(cursor.hasNext()){
            HandBook handBook = cursor.next();
            handBook.setNameTypeNews(nameType);
            jsonArray.add(handBook.toJson());
        }
        response.setDatas(jsonArray);

        return response;
    }

    @Override
    public HandBookResponse getHandbookById(String idHB) {
        HandBookResponse response = new HandBookResponse();
        if (!ObjectId.isValid(idHB)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(HandBook.class.getSimpleName());
        MongoCollection collectionType = jongo.getCollection(TypeNews.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<HandBook> cursor = collection.find(builder.toString(), new ObjectId(idHB)).as(HandBook.class);
        if(cursor.hasNext()){
            HandBook handBook = cursor.next();
            MongoCursor<TypeNews> cursorType = collectionType.find(builder.toString(), handBook.getTypeNews()).as(TypeNews.class);
            if(cursorType.hasNext()){
                handBook.setNameTypeNews(cursorType.next().getNameType());
            }
            response.setData(handBook.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }

        return response;
    }

    @Override
    public HandBookResponse createHandbook(String title, String author, String image, String typeNews,String nameTypeNews, String content) {
        HandBookResponse response = new HandBookResponse();
        if (!ObjectId.isValid(typeNews)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id typeNews không đúng.");
            return response;
        }
        HandBook handBook = new HandBook();
        ObjectId _id = new ObjectId();
        handBook.set_id(_id.toHexString());
        handBook.setTitle(title);
        handBook.setAuthor(author);
        handBook.setImage(image);
        handBook.setTypeNews(new ObjectId(typeNews));
        handBook.setNameTypeNews(nameTypeNews);
        handBook.setTimeCreate(Calendar.getInstance().getTimeInMillis());
        handBook.setContent(content);
        MongoPool.log(HandBook.class.getSimpleName(),handBook.toDocument());
        response.setData(handBook.toJson());
        return  response;
    }

    @Override
    public HandBookResponse editHandbook(String idHB, String title, String author, String image, String typeNews,String nameTypeNews, String content) {
        HandBookResponse response = new HandBookResponse();
        if (!ObjectId.isValid(typeNews)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id typeNews không đúng.");
            return response;
        }
        if (!ObjectId.isValid(idHB)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id News không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(HandBook.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<HandBook> cursor = collection.find(builder.toString(),new ObjectId(idHB)).limit(1).as(HandBook.class);
        if(cursor.hasNext()){
            HandBook handBook = cursor.next();
            handBook.setTitle(title);
            handBook.setAuthor(author);
            handBook.setImage(image);
            handBook.setTypeNews(new ObjectId(typeNews));
            handBook.setNameTypeNews(nameTypeNews);
            handBook.setContent(content);
            collection.update("{_id:#}", new ObjectId(idHB)).with("{$set:{title:#,author:#,image:#,typeNews:#,content:#}}",
                    handBook.getTitle(),handBook.getAuthor(),handBook.getImage(),handBook.getTypeNews(),handBook.getContent());
            response.setData(handBook.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
            return response;
        }
        return response;
    }

    @Override
    public HandBookResponse deleteHandbook(String idHB) {
        HandBookResponse response = new HandBookResponse();
        if (!ObjectId.isValid(idHB)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(HandBook.class.getSimpleName());
        collection.remove(new ObjectId(idHB));

        return response;
    }
}
