package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.PriceMarketResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.PriceMarket;
import vietung.it.dev.core.models.TypeNews;
import vietung.it.dev.core.services.PriceMarketService;

import java.util.Calendar;

public class PriceMarketServiceImp implements PriceMarketService {
    @Override
    public PriceMarketResponse addPriceMarket(String namePlace, long price, String typeNews, String nameTypeNews) {
        PriceMarketResponse response = new PriceMarketResponse();
        PriceMarket priceMarket = new PriceMarket();
        if (!ObjectId.isValid(typeNews)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id typeNews không đúng.");
            return response;
        }
        ObjectId id = new ObjectId();
        priceMarket.set_id(id.toHexString());
        priceMarket.setNamePlace(namePlace);
        priceMarket.setPrice(price);
        priceMarket.setTimeCreate(Calendar.getInstance().getTimeInMillis());
        priceMarket.setTypeNews(new ObjectId(typeNews));
        MongoPool.log(PriceMarket.class.getSimpleName(),priceMarket.toDocument());
        response.setData(priceMarket);
        return response;
    }

    @Override
    public PriceMarketResponse editPriceMarket(String idPM, String namePlace, long price) {
        PriceMarketResponse response = new PriceMarketResponse();
        if (!ObjectId.isValid(idPM)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id typeNews không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(PriceMarket.class.getSimpleName());
        MongoCollection collectionType = jongo.getCollection(TypeNews.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<PriceMarket> cursor = collection.find(builder.toString(),new ObjectId(idPM)).limit(1).as(PriceMarket.class);
        if(cursor.hasNext()){
            PriceMarket priceMarket = cursor.next();
            priceMarket.setNamePlace(namePlace);
            priceMarket.setPrice(price);
            priceMarket.setTimeCreate(Calendar.getInstance().getTimeInMillis());
            MongoCursor<TypeNews> cursorType = collectionType.find(builder.toString(),priceMarket.getTypeNews()).limit(1).as(TypeNews.class);
            if (cursorType.hasNext()){
                priceMarket.setNameTypeNews(cursorType.next().getNameType());
            }
            collection.update("{_id:#}", new ObjectId(idPM)).with("{$set:{namePlace:#,price:#,timeCreate:#}}",
                    priceMarket.getNamePlace(),priceMarket.getPrice(),priceMarket.getTimeCreate());
            response.setData(priceMarket);
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
            return response;
        }
        return response;
    }

    @Override
    public PriceMarketResponse deletePriceMarket(String idPM) {
        PriceMarketResponse response = new PriceMarketResponse();
        if (!ObjectId.isValid(idPM)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(PriceMarket.class.getSimpleName());
        collection.remove(new ObjectId(idPM));

        return response;
    }

    @Override
    public PriceMarketResponse getAllPriceMarket(String lastId,int ofset) {
        PriceMarketResponse response = new PriceMarketResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCollection collection = jongo.getCollection(PriceMarket.class.getSimpleName());

        MongoCursor<PriceMarket> cursor;
        if(lastId!=null && !lastId.equals("")){
            if (!ObjectId.isValid(lastId)) {
                response.setError(ErrorCode.NOT_A_OBJECT_ID);
                response.setMsg("Id không đúng.");
                return response;
            }
            builder.append("{_id:{$lt:#}}");
            cursor = collection.find(builder.toString(),new ObjectId(lastId)).sort("{_id:-1}").limit(ofset).as(PriceMarket.class);
        }else {
            cursor = collection.find().sort("{_id:-1}").limit(ofset).as(PriceMarket.class);
        }

        MongoCollection collectionType = jongo.getCollection(TypeNews.class.getSimpleName());
        JsonArray jsonArray = new JsonArray();
        while(cursor.hasNext()){
            PriceMarket priceMarket = cursor.next();
            MongoCursor<TypeNews> cursorType = collectionType.find(builder.toString(), priceMarket.getTypeNews()).as(TypeNews.class);
            if(cursorType.hasNext()){
                priceMarket.setNameTypeNews(cursorType.next().getNameType());
            }
            jsonArray.add(priceMarket.toJson());
        }
        response.setDatas(jsonArray);

        return response;
    }

    @Override
    public PriceMarketResponse getPriceMarketByType(String idType,String lastId,int ofset) {
        PriceMarketResponse response = new PriceMarketResponse();
        if (!ObjectId.isValid(idType)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCollection collection = jongo.getCollection(PriceMarket.class.getSimpleName());
        MongoCursor<PriceMarket> cursor = null;
        if(lastId!=null && !lastId.equals("")){
            if (!ObjectId.isValid(lastId)) {
                response.setError(ErrorCode.NOT_A_OBJECT_ID);
                response.setMsg("Id không đúng.");
                return response;
            }
            builder.append("{$and: [{typeNews: #},{_id:{$lt:#}}]}");
            cursor = collection.find(builder.toString(),new ObjectId(idType),new ObjectId(lastId)).sort("{_id:-1}").limit(ofset).as(PriceMarket.class);
        }else {
            builder.append("{$and: [{typeNews: #}]}");
            cursor = collection.find(builder.toString(),new ObjectId(idType)).sort("{_id:-1}").limit(ofset).as(PriceMarket.class);
        }
        MongoCollection collectionType = jongo.getCollection(TypeNews.class.getSimpleName());
        MongoCursor<TypeNews> cursorType = collectionType.find(builder.toString(), new ObjectId(idType)).as(TypeNews.class);
        String nameType = "";
        if(cursorType.hasNext()){
            nameType = cursorType.next().getNameType();
        }
        JsonArray jsonArray = new JsonArray();
        while(cursor.hasNext()){
            PriceMarket priceMarket = cursor.next();
            priceMarket.setNameTypeNews(nameType);
            jsonArray.add(priceMarket.toJson());
        }
        response.setDatas(jsonArray);

        return response;
    }
}
