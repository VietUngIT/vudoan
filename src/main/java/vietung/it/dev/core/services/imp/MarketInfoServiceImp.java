package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.MarketInfoResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.consts.Variable;
import vietung.it.dev.core.models.Category;
import vietung.it.dev.core.models.MarketInfo;
import vietung.it.dev.core.services.MarketInfoService;
import vietung.it.dev.core.services.UploadService;
import vietung.it.dev.core.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MarketInfoServiceImp implements MarketInfoService {
    @Override
    public int commentNews(String idNews, Boolean isCmt) {
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(MarketInfo.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<MarketInfo> cursor = collection.find(builder.toString(), new ObjectId(idNews)).as(MarketInfo.class);
        if(cursor.hasNext()){
            MarketInfo marketInfo = cursor.next();
            int cmt = marketInfo.getNumComment()>=0?marketInfo.getNumComment():0;
            if(isCmt){
                marketInfo.setNumComment(cmt+1);
            }else {
                marketInfo.setNumComment(cmt-1);
            }

            collection.update("{_id:#}", new ObjectId(idNews)).with("{$set:{numComment:#}}",marketInfo.getNumComment());
            return marketInfo.getNumComment();
        }
        return 0;
    }

    @Override
    public MarketInfoResponse deleteNewsMarketInfo(String id) throws Exception {
        MarketInfoResponse response = new MarketInfoResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(MarketInfo.class.getSimpleName());
        collection.remove(new ObjectId(id));
        return response;
    }

    @Override
    public MarketInfoResponse editTagsNewsMarketInfo(String id, String tags) throws Exception {
        MarketInfoResponse response = new MarketInfoResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id tin tức không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(MarketInfo.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<MarketInfo> cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(MarketInfo.class);
        if(cursor.hasNext()){
            MarketInfo marketInfo = cursor.next();
            JsonArray array = Utils.toJsonArray(tags);
            List<String> lstTag = new ArrayList<>();
            for (int i=0;i<array.size();i++){
                lstTag.add(array.get(i).getAsString().toLowerCase());
            }
            collection.update("{_id:#}", new ObjectId(id)).with("{$set:{tags:#}}",lstTag);
            marketInfo.setTags(lstTag);

            MongoCollection collectionIdCate = jongo.getCollection(Variable.MG_CATEGORY_MARKET_INFO);
            MongoCursor<Category> cursorCate = collectionIdCate.find("{_id:#}", new ObjectId(marketInfo.getIdCateNews())).as(Category.class);
            if(cursorCate.hasNext()){
                marketInfo.setNameCateNews(cursorCate.next().getName());
            }
            response.setData(marketInfo.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
            return response;
        }
        return response;
    }

    @Override
    public MarketInfoResponse editImageNewsMarketInfo(String id, String image) throws Exception {
        MarketInfoResponse response = new MarketInfoResponse();
        UploadService service = new UploadServiceImp();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id tin tức không đúng.");
            return response;
        }
        try {
            DB db = MongoPool.getDBJongo();
            Jongo jongo = new Jongo(db);
            String urlImage = service.uploadImage(image);
            MongoCollection collection = jongo.getCollection(MarketInfo.class.getSimpleName());
            StringBuilder builder = new StringBuilder();
            builder.append("{_id:#}");
            MongoCursor<MarketInfo> cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(MarketInfo.class);
            if(cursor.hasNext()){
                MarketInfo marketInfo = cursor.next();
                marketInfo.setImage(urlImage);
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{image:#}}",marketInfo.getImage());
                JsonObject object = new JsonObject();
                object.addProperty("image",marketInfo.getImage());
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
    public MarketInfoResponse editNewsmarketInfo(String id, String title, String author, String source, String idCateNews, String content) throws Exception {
        MarketInfoResponse response = new MarketInfoResponse();

        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id tin tức không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(MarketInfo.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<MarketInfo> cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(MarketInfo.class);
        if(cursor.hasNext()){
            MarketInfo marketInfo = cursor.next();
            if(idCateNews!=null){
                if (!ObjectId.isValid(idCateNews)) {
                    response.setError(ErrorCode.NOT_A_OBJECT_ID);
                    response.setMsg("Id danh mục tin tức thị trường không đúng.");
                    return response;
                }
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{idCateNews:#}}",idCateNews);
                marketInfo.setIdCateNews(idCateNews);
            }
            if(title!=null){
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{title:#}}",title);
                marketInfo.setTitle(title);
            }
            if(author!=null){
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{author:#}}",author);
                marketInfo.setAuthor(author);
            }
            if(source!=null){
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{source:#}}",source);
                marketInfo.setSource(source);
            }

            if(content!=null){
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{content:#}}",content);
                marketInfo.setContent(content);
            }

            MongoCollection collectionIdCate = jongo.getCollection(Variable.MG_CATEGORY_MARKET_INFO);
            MongoCursor<Category> cursorCate = collectionIdCate.find("{_id:#}", new ObjectId(marketInfo.getIdCateNews())).as(Category.class);
            if(cursorCate.hasNext()){
                marketInfo.setNameCateNews(cursorCate.next().getName());
            }
            response.setData(marketInfo.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
            return response;
        }
        return response;
    }

    @Override
    public MarketInfoResponse createNewsMarketInfo(String title, String author, String image, String source, String tags, String idCateNews, String content) throws Exception {
        MarketInfoResponse response = new MarketInfoResponse();
        UploadService service = new UploadServiceImp();
        if (!ObjectId.isValid(idCateNews)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id danh mục không đúng.");
            return response;
        }
        try {
            String urlImage = null;
            DB db = MongoPool.getDBJongo();
            Jongo jongo = new Jongo(db);

            if(image!=null){
                urlImage = service.uploadImage(image);
            }
            JsonArray array = Utils.toJsonArray(tags);
            List<String> lstTag = new ArrayList<>();
            for (int i=0;i<array.size();i++){
                lstTag.add(array.get(i).getAsString().toLowerCase());
            }
            MarketInfo marketInfo = new MarketInfo();
            ObjectId _id = new ObjectId();
            marketInfo.set_id(_id.toHexString());
            marketInfo.setIdCateNews(idCateNews);
            marketInfo.setImage(urlImage);
            marketInfo.setTitle(title);
            marketInfo.setTimeCreate(Calendar.getInstance().getTimeInMillis());
            marketInfo.setAuthor(author);
            marketInfo.setNumComment(0);
            marketInfo.setSource(source);
            marketInfo.setTags(lstTag);
            marketInfo.setContent(content);
            MongoPool.log(MarketInfo.class.getSimpleName(),marketInfo.toDocument());

            MongoCollection collectionIdCate = jongo.getCollection(Variable.MG_CATEGORY_MARKET_INFO);
            MongoCursor<Category> cursorCate = collectionIdCate.find("{_id:#}", new ObjectId(idCateNews)).as(Category.class);
            if(cursorCate.hasNext()){
                marketInfo.setNameCateNews(cursorCate.next().getName());
            }
            response.setData(marketInfo.toJson());

        }catch (Exception e){
            e.printStackTrace();
        }

        return  response;
    }

    @Override
    public MarketInfoResponse getNewsMarketInfoById(String id) {
        MarketInfoResponse response = new MarketInfoResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(MarketInfo.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<MarketInfo> cursor = collection.find(builder.toString(), new ObjectId(id)).as(MarketInfo.class);
        if(cursor.hasNext()){
            MarketInfo marketInfo = cursor.next();
            response.setData(marketInfo.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }

        return response;
    }

    @Override
    public MarketInfoResponse getAllNewsMarketInfoByCate(int page, int ofs, String idcate) {
        MarketInfoResponse response = new MarketInfoResponse();
        if (!ObjectId.isValid(idcate)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<MarketInfo> cursor = null;
        MongoCollection collection = jongo.getCollection(MarketInfo.class.getSimpleName());
        builder.append("{$and: [{idCateNews: #}]}");
        cursor = collection.find(builder.toString(),idcate).sort("{timeCreate:-1}").skip(page*ofs).limit(ofs).as(MarketInfo.class);
        JsonArray jsonArray = new JsonArray();
        response.setTotal(cursor.count());
        while(cursor.hasNext()){
            MarketInfo marketInfo = cursor.next();
            jsonArray.add(marketInfo.toJson());
        }
        response.setArray(jsonArray);

        return response;
    }
}
