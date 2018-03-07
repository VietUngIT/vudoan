package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.NewsResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.CommentsNews;
import vietung.it.dev.core.models.News;
import vietung.it.dev.core.models.Users;
import vietung.it.dev.core.services.NewsService;
import vietung.it.dev.core.utils.Utils;

import java.util.Calendar;

public class NewsServiceImp implements NewsService {
    @Override
    public NewsResponse likeNews(String idNews, Boolean isLike){
        NewsResponse response = new NewsResponse();
        if (!ObjectId.isValid(idNews)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(News.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<News> cursor = collection.find(builder.toString(), new ObjectId(idNews)).as(News.class);
        if(cursor.hasNext()){
            News news = cursor.next();
            int like = news.getLikes()>=0?news.getLikes():0;
            if(isLike){
                news.setLikes(like+1);
                collection.update("{_id:#}", new ObjectId(idNews)).with("{$set:{likes:#}}",news.getLikes());
            }else {
                news.setLikes(like-1);
                collection.update("{_id:#}", new ObjectId(idNews)).with("{$set:{likes:#}}",news.getLikes());
            }
            response.setData(news.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }
        return response;
    }

    @Override
    public NewsResponse viewNews(String idNews) throws Exception {
        NewsResponse response = new NewsResponse();
        if (!ObjectId.isValid(idNews)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(News.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<News> cursor = collection.find(builder.toString(), new ObjectId(idNews)).as(News.class);
        if(cursor.hasNext()){
            News news = cursor.next();
            int view = news.getViews()>=0?news.getViews():0;
            news.setViews(view+1);
            collection.update("{_id:#}", new ObjectId(idNews)).with("{$set:{views:#}}",news.getViews());
            response.setData(news.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }
        return response;
    }

    @Override
    public NewsResponse deleteNews(String idNews) {
        NewsResponse response = new NewsResponse();
        if (!ObjectId.isValid(idNews)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(News.class.getSimpleName());
        collection.remove(new ObjectId(idNews));

        return response;
    }

    @Override
    public NewsResponse getNewsById(String idNews) {
        NewsResponse response = new NewsResponse();
        if (!ObjectId.isValid(idNews)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(News.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<News> cursor = collection.find(builder.toString(), new ObjectId(idNews)).as(News.class);
        if(cursor.hasNext()){
            News news = cursor.next();
            response.setData(news.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }

        return response;
    }

    @Override
    public NewsResponse getAllNews(long tlast, int offer) {
        NewsResponse response = new NewsResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        builder.append("{timeCreate:{$lt:#}}");
        MongoCollection collection = jongo.getCollection(News.class.getSimpleName());
        MongoCursor<News> cursor = collection.find(builder.toString(),tlast).sort("{timeCreate:-1}").limit(offer).as(News.class);
        JsonArray jsonArray = new JsonArray();
        while(cursor.hasNext()){
            News news = cursor.next();
            jsonArray.add(news.toJson());
        }
        response.setDatas(jsonArray);

        return response;
    }

    @Override
    public NewsResponse getAllNewsByType(long tlast, int offer, int idType) {
        NewsResponse response = new NewsResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        if(tlast>0){
            builder.append("{$and: [{idTypeNews: #},{timeCreate:{$lt:#}}]}");
        }else {
            builder.append("{$and: [{idTypeNews: #}]}");
        }
        MongoCollection collection = jongo.getCollection(News.class.getSimpleName());
        MongoCursor<News> cursor = collection.find(builder.toString(),idType,tlast).sort("{timeCreate:-1}").limit(offer).as(News.class);
        JsonArray jsonArray = new JsonArray();
        while(cursor.hasNext()){
            News news = cursor.next();
            jsonArray.add(news.toJson());
        }
        response.setDatas(jsonArray);

        return response;
    }



}
