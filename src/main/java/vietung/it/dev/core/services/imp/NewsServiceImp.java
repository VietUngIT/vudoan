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
import vietung.it.dev.core.models.TypeNews;
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
    public int commentNews(String idNews, Boolean isCmt) {
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(News.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<News> cursor = collection.find(builder.toString(), new ObjectId(idNews)).as(News.class);
        if(cursor.hasNext()){
            News news = cursor.next();
            int cmt = news.getComments()>=0?news.getComments():0;
            if(isCmt){
                news.setComments(cmt+1);
            }else {
                news.setComments(cmt-1);
            }

            collection.update("{_id:#}", new ObjectId(idNews)).with("{$set:{comments:#}}",news.getComments());
            return news.getComments();
        }
        return 0;
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
        MongoCollection collectionType = jongo.getCollection(TypeNews.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<News> cursor = collection.find(builder.toString(), new ObjectId(idNews)).as(News.class);
        if(cursor.hasNext()){
            News news = cursor.next();
            MongoCursor<TypeNews> cursorType = collectionType.find(builder.toString(), news.getTypeNews()).as(TypeNews.class);
            if(cursorType.hasNext()){
                news.setNameTypeNews(cursorType.next().getNameType());
            }
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
        MongoCollection collection = jongo.getCollection(News.class.getSimpleName());
        MongoCursor<News> cursor;
        if(tlast>0){
            builder.append("{timeCreate:{$lt:#}}");
            cursor = collection.find(builder.toString(),tlast).sort("{timeCreate:-1}").limit(offer).as(News.class);
        }else {
            cursor = collection.find().sort("{timeCreate:-1}").limit(offer).as(News.class);

        }

        MongoCollection collectionType = jongo.getCollection(TypeNews.class.getSimpleName());
        JsonArray jsonArray = new JsonArray();
        while(cursor.hasNext()){
            News news = cursor.next();
            MongoCursor<TypeNews> cursorType = collectionType.find(builder.toString(), news.getTypeNews()).as(TypeNews.class);
            if(cursorType.hasNext()){
                news.setNameTypeNews(cursorType.next().getNameType());
            }
            jsonArray.add(news.toJson());
        }
        response.setDatas(jsonArray);

        return response;
    }

    @Override
    public NewsResponse getAllNewsByType(long tlast, int offer, String idType) {
        NewsResponse response = new NewsResponse();
        if (!ObjectId.isValid(idType)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<News> cursor = null;
        MongoCollection collection = jongo.getCollection(News.class.getSimpleName());
        if(tlast>0){
            builder.append("{$and: [{typeNews: #},{timeCreate:{$lt:#}}]}");
            cursor = collection.find(builder.toString(),new ObjectId(idType),tlast).sort("{timeCreate:-1}").limit(offer).as(News.class);
        }else {
            builder.append("{$and: [{typeNews: #}]}");
            cursor = collection.find(builder.toString(),new ObjectId(idType)).sort("{timeCreate:-1}").limit(offer).as(News.class);
        }
        MongoCollection collectionType = jongo.getCollection(TypeNews.class.getSimpleName());
        MongoCursor<TypeNews> cursorType = collectionType.find(builder.toString(), new ObjectId(idType)).as(TypeNews.class);
        String nameType = "";
        if(cursorType.hasNext()){
            nameType = cursorType.next().getNameType();
        }
        JsonArray jsonArray = new JsonArray();
        while(cursor.hasNext()){
            News news = cursor.next();
            news.setNameTypeNews(nameType);
            jsonArray.add(news.toJson());
        }
        response.setDatas(jsonArray);

        return response;
    }

    @Override
    public NewsResponse createNews(String title, String shortDescription, String author, String image, String source, String tags, String typeNews,String nameTypeNews, String content) {
        NewsResponse response = new NewsResponse();
        if (!ObjectId.isValid(typeNews)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id typeNews không đúng.");
            return response;
        }
        News news = new News();
        ObjectId _id = new ObjectId();
        news.set_id(_id.toHexString());
        news.setTitle(title);
        news.setShortDescription(shortDescription);
        news.setAuthor(author);
        news.setImage(image);
        news.setSource(source);
        news.setTags(tags);
        news.setViews(0);
        news.setLikes(0);
        news.setComments(0);
        news.setTypeNews(new ObjectId(typeNews));
        news.setNameTypeNews(nameTypeNews);
        news.setTimeCreate(Calendar.getInstance().getTimeInMillis());
        news.setContent(content);
        MongoPool.log(News.class.getSimpleName(),news.toDocument());
        response.setData(news.toJson());
        return  response;
    }

    @Override
    public NewsResponse editNews(String idNews,String title, String shortDescription, String author, String image, String source, String tags, String typeNews,String nameTypeNews, String content) {
        NewsResponse response = new NewsResponse();
        if (!ObjectId.isValid(typeNews)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id typeNews không đúng.");
            return response;
        }
        if (!ObjectId.isValid(idNews)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id News không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(News.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<News> cursor = collection.find(builder.toString(),new ObjectId(idNews)).limit(1).as(News.class);
        if(cursor.hasNext()){
            News news = cursor.next();
            news.setTitle(title);
            news.setShortDescription(shortDescription);
            news.setAuthor(author);
            news.setImage(image);
            news.setSource(source);
            news.setTags(tags);
            news.setTypeNews(new ObjectId(typeNews));
            news.setNameTypeNews(nameTypeNews);
            news.setContent(content);
            collection.update("{_id:#}", new ObjectId(idNews)).with("{$set:{title:#,shortDescription:#,author:#,image:#,source:#,tags:#,typeNews:#,content:#}}",
                    news.getTitle(),news.getShortDescription(),news.getAuthor(),news.getImage(),news.getSource(),news.getTags(),news.getTypeNews(),news.getContent());
            response.setData(news.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
            return response;
        }
        return response;
    }

}
