package vietung.it.dev.core.services.imp;

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
    public NewsResponse commentNews(String idNews, String phone, String content) throws Exception {
        NewsResponse response = new NewsResponse();
        if (!ObjectId.isValid(idNews)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        Users users = Utils.getUserByPhone(phone);
        if(users != null){
            CommentsNews commentsNews = new CommentsNews();
            ObjectId id = new ObjectId();
            commentsNews.set_id(id.toHexString());
            commentsNews.setName(users.getName());
            commentsNews.setPhone(users.getPhone());
            commentsNews.setAvatar(users.getAvatar());
            commentsNews.setContent(content);
            commentsNews.setIdNews(idNews);
            commentsNews.setTimeCreate(Calendar.getInstance().getTimeInMillis());
            MongoPool.log(CommentsNews.class.getSimpleName(),commentsNews.toDocument());
            response.setData(commentsNews.toJson());
        }else {
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Số điện thoại này chưa được đăng ký.");
        }
        return response;
    }

    @Override
    public NewsResponse deleteNews(String idNews, String phone) {
        NewsResponse response = new NewsResponse();
        if (!ObjectId.isValid(idNews)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        Users users = Utils.getUserByPhone(phone);
        if(users != null){
            if(users.getRoles()>1){
                DB db = MongoPool.getDBJongo();
                Jongo jongo = new Jongo(db);
                MongoCollection collection = jongo.getCollection(News.class.getSimpleName());
                collection.remove(new ObjectId(idNews));
            }else {
                response.setError(ErrorCode.LESS_ROLE);
                response.setMsg("Không có đủ quyền thực hiện.");
            }
        }else {
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Số điện thoại này chưa được đăng ký.");
        }
        return response;
    }
}
