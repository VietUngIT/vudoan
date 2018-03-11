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
import vietung.it.dev.core.models.Users;
import vietung.it.dev.core.services.CommentNewsService;
import vietung.it.dev.core.services.NewsService;
import vietung.it.dev.core.utils.Utils;

import java.util.Calendar;

public class CommentNewsServiceImp implements CommentNewsService {
    @Override
    public NewsResponse commentNews(String idNews, String phone, String content) throws Exception {
        NewsResponse response = new NewsResponse();
        NewsService service = new NewsServiceImp();
        if (!ObjectId.isValid(idNews)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        Users users = Utils.getUserByPhone(phone);
        CommentsNews commentsNews = new CommentsNews();
        ObjectId id = new ObjectId();
        commentsNews.set_id(id.toHexString());
        commentsNews.setPhone(users.getPhone());
        commentsNews.setContent(content);
        commentsNews.setIdNews(idNews);
        commentsNews.setTimeCreate(Calendar.getInstance().getTimeInMillis());
        MongoPool.log(CommentsNews.class.getSimpleName(),commentsNews.toDocument());
        int num = service.commentNews(idNews,true);
        response.setNumCmtByNew(num);
        response.setData(commentsNews.toJson());

        return response;
    }

    @Override
    public NewsResponse deleteCommentNews(String idCmt, String phone) {
        NewsResponse response = new NewsResponse();
        NewsService service = new NewsServiceImp();
        if (!ObjectId.isValid(idCmt)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        Users users = Utils.getUserByPhone(phone);
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(CommentsNews.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<CommentsNews> cursor = collection.find(builder.toString(), new ObjectId(idCmt)).as(CommentsNews.class);
        if(cursor.hasNext()){
            CommentsNews commentsNews = cursor.next();
            if(users.getRoles()==2 || users.getRoles()==3 || users.getPhone().equals(commentsNews.getPhone())){
                int num = service.commentNews(commentsNews.getIdNews(),false);
                response.setNumCmtByNew(num);
                collection.remove(new ObjectId(idCmt));
            }else {
                response.setError(ErrorCode.NO_ROLE_DELETE);
                response.setMsg("Không đủ quyền để xóa bình luận.");
            }
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }

        return response;
    }

    @Override
    public NewsResponse getCommentbyNews(String idNews, int offset, long timeLast) {
        NewsResponse response = new NewsResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCollection collection = jongo.getCollection(CommentsNews.class.getSimpleName());
        MongoCursor<CommentsNews> cursor;
        if(timeLast>0){
            builder.append("{$and: [{idNews: #},{timeCreate:{$lt:#}}]}");
            cursor = collection.find(builder.toString(),idNews,timeLast).sort("{timeCreate:-1}").limit(offset).as(CommentsNews.class);
        }else {
            builder.append("{$and: [{idNews: #}]}");
            cursor = collection.find(builder.toString(),idNews).sort("{timeCreate:-1}").limit(offset).as(CommentsNews.class);
        }
        JsonArray jsonArray = new JsonArray();
        while(cursor.hasNext()){
            CommentsNews commentsNews = cursor.next();
            Users users = Utils.getUserByPhone(commentsNews.getPhone());
            commentsNews.setName(users.getName());
            commentsNews.setAvatar(users.getAvatar());
            jsonArray.add(commentsNews.toJson());
        }
        response.setDatas(jsonArray);

        return response;
    }
}
