package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.NewsResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.consts.Variable;
import vietung.it.dev.core.models.*;
import vietung.it.dev.core.services.NewsService;
import vietung.it.dev.core.services.UploadService;
import vietung.it.dev.core.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewsServiceImp implements NewsService {
    @Override
    public NewsResponse likeNews(String idNews, Boolean isLike,String phone){
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
            int like = news.getNumLike()>=0?news.getNumLike():0;
            if(isLike){
                news.setNumLike(like+1);
                collection.update("{_id:#}", new ObjectId(idNews)).with("{$set:{numLike:#}}",news.getNumLike());
                collection.update("{_id:#}", new ObjectId(idNews)).with("{ $push: {userLike:#}}",phone);
            }else {
                news.setNumLike(like-1);
                collection.update("{_id:#}", new ObjectId(idNews)).with("{$set:{numLike:#}}",news.getNumLike());
                collection.update("{_id:#}", new ObjectId(idNews)).with("{ $pull: {userLike:#}}",phone);
            }
            JsonObject object = new JsonObject();
            object.addProperty("numLike",news.getNumLike());
            response.setData(object);
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
            int view = news.getNumView()>=0?news.getNumView():0;
            news.setNumView(view+1);
            collection.update("{_id:#}", new ObjectId(idNews)).with("{$set:{numView:#}}",news.getNumView());
            JsonObject object = new JsonObject();
            object.addProperty("numView",news.getNumView());
            response.setData(object);
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
            int cmt = news.getNumComment()>=0?news.getNumComment():0;
            if(isCmt){
                news.setNumComment(cmt+1);
            }else {
                news.setNumComment(cmt-1);
            }

            collection.update("{_id:#}", new ObjectId(idNews)).with("{$set:{numComment:#}}",news.getNumComment());
            return news.getNumComment();
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
    public NewsResponse getNewsById(String idNews,String phone) {
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
            List<String> userLike = news.getUserLike();
            if(userLike!=null && userLike.contains(phone)){
                news.setIsLiked(true);
            }else{
                news.setIsLiked(false);
            }
            response.setData(news.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }

        return response;
    }

    @Override
    public NewsResponse getAllNewsByCateWithNewest(int page, int ofset, String idcate) {
        NewsResponse response = new NewsResponse();
        if (!ObjectId.isValid(idcate)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<News> cursor = null;
        MongoCollection collection = jongo.getCollection(News.class.getSimpleName());
        builder.append("{$and: [{idCateNews: #}]}");
        cursor = collection.find(builder.toString(),idcate).sort("{timeCreate:-1}").skip(page*ofset).limit(ofset).as(News.class);
        JsonArray jsonArray = new JsonArray();
        response.setTotal(cursor.count());
        while(cursor.hasNext()){
            News news = cursor.next();
            jsonArray.add(news.toJson());
        }
        response.setDatas(jsonArray);

        return response;
    }

    @Override
    public NewsResponse getAllNewsByCateWithFavorite(int page, int ofset, String idcate) {
        NewsResponse response = new NewsResponse();
        if (!ObjectId.isValid(idcate)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<News> cursor = null;
        MongoCollection collection = jongo.getCollection(News.class.getSimpleName());
        builder.append("{$and: [{idCateNews: #}]}");
        cursor = collection.find(builder.toString(),idcate).sort("{numLike:-1}").skip(page*ofset).limit(ofset).as(News.class);
        JsonArray jsonArray = new JsonArray();
        response.setTotal(cursor.count());
        while(cursor.hasNext()){
            News news = cursor.next();
            jsonArray.add(news.toJson());
        }
        response.setDatas(jsonArray);

        return response;
    }

    @Override
    public NewsResponse getAllNewsByCateWithPopular(int page, int ofset, String idcate) {
        NewsResponse response = new NewsResponse();
        if (!ObjectId.isValid(idcate)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<News> cursor = null;
        MongoCollection collection = jongo.getCollection(News.class.getSimpleName());
        builder.append("{$and: [{idCateNews: #}]}");
        cursor = collection.find(builder.toString(),idcate).sort("{numView:-1}").skip(page*ofset).limit(ofset).as(News.class);
        JsonArray jsonArray = new JsonArray();
        response.setTotal(cursor.count());
        while(cursor.hasNext()){
            News news = cursor.next();
            jsonArray.add(news.toJson());
        }
        response.setDatas(jsonArray);

        return response;
    }

    @Override
    public NewsResponse createNews(String title, String shortDescription, String author, String image, String source, String tags, String idCateNews,String content) throws Exception {
        NewsResponse response = new NewsResponse();
        UploadService service = new UploadServiceImp();
        if (!ObjectId.isValid(idCateNews)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id typeNews không đúng.");
            return response;
        }
        try {
            String urlImage = null;
            DB db = MongoPool.getDBJongo();
            Jongo jongo = new Jongo(db);

            if(image!=null){
                urlImage = service.uploadImage(image);
            }
            System.out.println("tags: "+tags);
            JsonArray array = Utils.toJsonArray(tags);
            List<String> lstTag = new ArrayList<>();
            for (int i=0;i<array.size();i++){
                lstTag.add(array.get(i).getAsString());
            }
            News news = new News();
            ObjectId _id = new ObjectId();
            news.set_id(_id.toHexString());
            news.setTitle(title);
            news.setShortDescription(shortDescription);
            news.setAuthor(author);
            news.setImage(urlImage);
            news.setSource(source);
            news.setTags(lstTag);
            news.setNumView(0);
            news.setNumLike(0);
            news.setNumComment(0);
            news.setIdCateNews(idCateNews);
            news.setTimeCreate(Calendar.getInstance().getTimeInMillis());
            news.setContent(content);
            List<String> userLike = new ArrayList<>();
            news.setUserLike(userLike);
            MongoPool.log(News.class.getSimpleName(),news.toDocument());

            MongoCollection collectionIdCate = jongo.getCollection(Variable.MG_CATEGORY_NEWS);
            MongoCursor<Category> cursorCate = collectionIdCate.find("{_id:#}", new ObjectId(idCateNews)).as(Category.class);
            if(cursorCate.hasNext()){
                news.setNameCateNews(cursorCate.next().getName());
            }
            response.setData(news.toJson());

        }catch (Exception e){
            e.printStackTrace();
        }

        return  response;
    }

    @Override
    public NewsResponse editNews(String idNews,String title, String shortDescription, String author, String source,String idCateNews, String content) {
        NewsResponse response = new NewsResponse();

        if (!ObjectId.isValid(idNews)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id tin tức không đúng.");
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
            if(idCateNews!=null){
                if (!ObjectId.isValid(idCateNews)) {
                    response.setError(ErrorCode.NOT_A_OBJECT_ID);
                    response.setMsg("Id danh mục tin tức không đúng.");
                    return response;
                }
                collection.update("{_id:#}", new ObjectId(idNews)).with("{$set:{idCateNews:#}}",idCateNews);
                news.setIdCateNews(idCateNews);
            }
            if(title!=null){
                collection.update("{_id:#}", new ObjectId(idNews)).with("{$set:{title:#}}",title);
                news.setTitle(title);
            }
            if(shortDescription!=null){
                collection.update("{_id:#}", new ObjectId(idNews)).with("{$set:{shortDescription:#}}",shortDescription);
                news.setShortDescription(shortDescription);
            }
            if(author!=null){
                collection.update("{_id:#}", new ObjectId(idNews)).with("{$set:{author:#}}",author);
                news.setAuthor(author);
            }
            if(source!=null){
                collection.update("{_id:#}", new ObjectId(idNews)).with("{$set:{source:#}}",source);
                news.setSource(source);
            }

            if(content!=null){
                collection.update("{_id:#}", new ObjectId(idNews)).with("{$set:{content:#}}",content);
                news.setContent(content);
            }

            MongoCollection collectionIdCate = jongo.getCollection(Variable.MG_CATEGORY_NEWS);
            MongoCursor<Category> cursorCate = collectionIdCate.find("{_id:#}", new ObjectId(news.getIdCateNews())).as(Category.class);
            if(cursorCate.hasNext()){
                news.setNameCateNews(cursorCate.next().getName());
            }
            response.setData(news.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
            return response;
        }
        return response;
    }

    @Override
    public NewsResponse editImageNews(String idNews, String image) {
        NewsResponse response = new NewsResponse();
        UploadService service = new UploadServiceImp();
        if (!ObjectId.isValid(idNews)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id tin tức không đúng.");
            return response;
        }
        try {
            DB db = MongoPool.getDBJongo();
            Jongo jongo = new Jongo(db);
            String urlImage = service.uploadImage(image);
            MongoCollection collection = jongo.getCollection(News.class.getSimpleName());
            StringBuilder builder = new StringBuilder();
            builder.append("{_id:#}");
            MongoCursor<News> cursor = collection.find(builder.toString(),new ObjectId(idNews)).limit(1).as(News.class);
            if(cursor.hasNext()){
                News news = cursor.next();
                news.setImage(urlImage);
                collection.update("{_id:#}", new ObjectId(idNews)).with("{$set:{image:#}}",news.getImage());
                JsonObject object = new JsonObject();
                object.addProperty("image",news.getImage());
                response.setData(object);
            }else {
                response.setError(ErrorCode.ID_NOT_EXIST);
                response.setMsg("Id không tồn tại.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public NewsResponse editTagsNews(String idNews, String tags) {
        NewsResponse response = new NewsResponse();
        if (!ObjectId.isValid(idNews)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id tin tức không đúng.");
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

            List<String> lstTag = new ArrayList<>();
            if(tags.indexOf("[")==-1){
                String temp[]=tags.split(",");
                for(int i=0;i<temp.length;i++){
                    lstTag.add(temp[i]);
                }
            }else{
                String temp = tags.replace("List ","");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(temp);
                JsonArray array = Utils.toJsonArray(stringBuilder.toString());
                for (int i=0;i<array.size();i++){
                    lstTag.add(array.get(i).getAsString());
                }
            }

            collection.update("{_id:#}", new ObjectId(idNews)).with("{$set:{tags:#}}",lstTag);
            news.setTags(lstTag);

            MongoCollection collectionIdCate = jongo.getCollection(Variable.MG_CATEGORY_NEWS);
            MongoCursor<Category> cursorCate = collectionIdCate.find("{_id:#}", new ObjectId(news.getIdCateNews())).as(Category.class);
            if(cursorCate.hasNext()){
                news.setNameCateNews(cursorCate.next().getName());
            }
            response.setData(news.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
            return response;
        }
        return response;
    }

}
