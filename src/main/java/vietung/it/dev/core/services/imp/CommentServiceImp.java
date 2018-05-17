package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.CommentResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.consts.Variable;
import vietung.it.dev.core.models.CommentsNews;
import vietung.it.dev.core.models.Users;
import vietung.it.dev.core.services.AgriTechService;
import vietung.it.dev.core.services.CommentService;
import vietung.it.dev.core.services.MarketInfoService;
import vietung.it.dev.core.services.NewsService;
import vietung.it.dev.core.utils.Utils;

import java.util.Calendar;

public class CommentServiceImp implements CommentService {

    @Override
    public CommentResponse commentNews(String idNews, String phone, String content, int collection) throws Exception {
        CommentResponse response = new CommentResponse();
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
        commentsNews.setIdUser(users.get_id());
        commentsNews.setTimeCreate(Calendar.getInstance().getTimeInMillis());
        if(collection== Variable.COMMENTS_NEWS){
            MongoPool.log(Variable.MG_COMMENTS_NEWS,commentsNews.toDocument());
            NewsService service = new NewsServiceImp();
            service.commentNews(idNews,true);
            response.setData(commentsNews.toJson());
        }else if(collection == Variable.COMMENTS_AGRI_TECH){
            MongoPool.log(Variable.MG_COMMENTS_AGRI_TECH,commentsNews.toDocument());
            AgriTechService service = new AgriTechServiceImp();
            service.commentNews(idNews,true);
            response.setData(commentsNews.toJson());
        }else if(collection == Variable.COMMENTS_MARKET_INFO){
            MongoPool.log(Variable.MG_COMMENTS_MARKET_INFO,commentsNews.toDocument());
            MarketInfoService service = new MarketInfoServiceImp();
            service.commentNews(idNews,true);
            response.setData(commentsNews.toJson());
        }else {
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
        }
        return response;
    }

    @Override
    public CommentResponse deleteCommentNews(String idCmnt, String phone, int collection) throws Exception {
        CommentResponse response = new CommentResponse();
        NewsService newsService = new NewsServiceImp();
        MarketInfoService marketInfoService = new MarketInfoServiceImp();
        AgriTechService service = new AgriTechServiceImp();
        if (!ObjectId.isValid(idCmnt)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        Users users = Utils.getUserByPhone(phone);
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);

        String strCollection = getCollection(collection);
        if(strCollection==null){
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }

        MongoCollection mgcollection = jongo.getCollection(strCollection);
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<CommentsNews> cursor = mgcollection.find(builder.toString(), new ObjectId(idCmnt)).as(CommentsNews.class);
        if(cursor.hasNext()){
            CommentsNews commentsNews = cursor.next();
            if(users.getRoles()==2 || users.getRoles()==3 || users.get_id().equals(commentsNews.getIdUser())){
                if(collection== Variable.COMMENTS_NEWS){
                    newsService.commentNews(commentsNews.getIdNews(),false);
                    mgcollection.remove(new ObjectId(idCmnt));
                }else if(collection== Variable.COMMENTS_AGRI_TECH){
                    service.commentNews(commentsNews.getIdNews(),false);
                    mgcollection.remove(new ObjectId(idCmnt));
                }else if(collection== Variable.COMMENTS_MARKET_INFO){
                    marketInfoService.commentNews(commentsNews.getIdNews(),false);
                    mgcollection.remove(new ObjectId(idCmnt));
                }else{
                    response.setError(ErrorCode.INVALID_PARAMS);
                    response.setMsg("Invalid params.");
                    return response;
                }

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
    public CommentResponse editCommentNews(String idCmnt, String phone, String content, int collection) throws Exception {
        CommentResponse response = new CommentResponse();
        if (!ObjectId.isValid(idCmnt)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        Users users = Utils.getUserByPhone(phone);
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);

        String strCollection = getCollection(collection);
        if(strCollection==null){
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }

        MongoCollection mgcollection = jongo.getCollection(strCollection);
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<CommentsNews> cursor = mgcollection.find(builder.toString(), new ObjectId(idCmnt)).as(CommentsNews.class);
        if(cursor.hasNext()){
            CommentsNews commentsNews = cursor.next();
            if(commentsNews.getIdUser().equals(users.get_id())){
                mgcollection.update("{_id:#}", new ObjectId(idCmnt)).with("{$set:{content:#}}",content);
            }else {
                response.setError(ErrorCode.NO_ROLE_DELETE);
                response.setMsg("Không đủ quyền để sửa bình luận.");
            }
        }
        return response;
    }

    @Override
    public CommentResponse getCommentbyNews(String idNews, int offset, int page, int collection) throws Exception {
        CommentResponse response = new CommentResponse();
        if (!ObjectId.isValid(idNews)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }

        String strCollection = getCollection(collection);
        if(strCollection==null){
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Invalid params.");
            return response;
        }

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection mgcollection = jongo.getCollection(strCollection);

        StringBuilder builder = new StringBuilder();
        builder.append("{$and: [{idNews: #}]}");
        MongoCursor<CommentsNews> cursor = mgcollection.find(builder.toString(),idNews).sort("{timeCreate:1}").skip(page*offset).limit(offset).as(CommentsNews.class);
        JsonArray jsonArray = new JsonArray();
        response.setTotal(cursor.count());
        while(cursor.hasNext()){
            CommentsNews commentsNews = cursor.next();
            if ((commentsNews.getIdUser()!=null && ObjectId.isValid(commentsNews.getIdUser()))){
                Users users = Utils.getUserById(commentsNews.getIdUser());
                commentsNews.setName(users.getName());
                commentsNews.setAvatar(users.getAvatar());
            }
            jsonArray.add(commentsNews.toJson());
        }
        response.setArray(jsonArray);

        return response;
    }

    private String getCollection(int type){
        if(type== Variable.COMMENTS_NEWS){
            return Variable.MG_COMMENTS_NEWS;
        }
        if(type== Variable.COMMENTS_AGRI_TECH){
            return Variable.MG_COMMENTS_AGRI_TECH;
        }
        if(type== Variable.COMMENTS_MARKET_INFO){
            return Variable.MG_COMMENTS_MARKET_INFO;
        }
        return null;
    }
}
