package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.MarketPriceResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.CategoryMarketPrice;
import vietung.it.dev.core.models.MarketPrice;
import vietung.it.dev.core.services.MarketPriceService;
import vietung.it.dev.core.utils.Utils;

import java.util.Calendar;

public class MarketPriceServiceImp implements MarketPriceService {

    @Override
    public MarketPriceResponse deleteMarketPrice(String id) throws Exception{
        MarketPriceResponse response = new MarketPriceResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(MarketPrice.class.getSimpleName());
        collection.remove(new ObjectId(id));

        return response;
    }

    @Override
    public MarketPriceResponse createMarketPrice(String idCate, String name, long price, String place, String unit, String note) throws Exception{
        MarketPriceResponse response = new MarketPriceResponse();
        if (!ObjectId.isValid(idCate)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id typeNews không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);

        MarketPrice marketPrice = new MarketPrice();
        ObjectId _id = new ObjectId();
        marketPrice.set_id(_id.toHexString());
        marketPrice.setIdCate(idCate);
        marketPrice.setName(name);
        marketPrice.setPrice(price);
        marketPrice.setUnit(unit);
        marketPrice.setPlace(place);
        marketPrice.setNote(note);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        marketPrice.setTimeCreate(calendar.getTimeInMillis());
        MongoPool.log(MarketPrice.class.getSimpleName(),marketPrice.toDocument());

        MongoCollection collectionIdCate = jongo.getCollection(CategoryMarketPrice.class.getSimpleName());
        MongoCursor<CategoryMarketPrice> cursorCate = collectionIdCate.find("{_id:#}", new ObjectId(idCate)).as(CategoryMarketPrice.class);
        if(cursorCate.hasNext()){
            marketPrice.setNameCate(cursorCate.next().getName());
        }
        response.setData(marketPrice.toJson());

        return  response;
    }

    @Override
    public MarketPriceResponse getAllTypeMarketPrice(int ofset) throws Exception {
        MarketPriceResponse response = new MarketPriceResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collectionIdCate = jongo.getCollection(CategoryMarketPrice.class.getSimpleName());
        MongoCollection collection = jongo.getCollection(MarketPrice.class.getSimpleName());
        MongoCursor<CategoryMarketPrice> cursorCate = collectionIdCate.find().as(CategoryMarketPrice.class);
        MongoCursor<MarketPrice> cursor = null;
        JsonArray root = new JsonArray();
        while (cursorCate.hasNext()){
            CategoryMarketPrice categoryMarketPrice = cursorCate.next();
            cursor = collection.find("{idCate:#}",categoryMarketPrice.get_id()).limit(ofset).as(MarketPrice.class);
            JsonObject object = new JsonObject();
            JsonArray array = new JsonArray();
            while(cursor.hasNext()){
                array.add(cursor.next().toJson());
            }
            if(array.size()>0){
                object.addProperty("cate",categoryMarketPrice.getName());
                object.addProperty("size",cursor.count());
                object.add("data",array);
                root.add(object);
            }

        }
        response.setArray(root);
        return response;
    }

    @Override
    public MarketPriceResponse getMarketPriceByCate(String idCate, int ofset, int page) throws Exception {
        MarketPriceResponse response = new MarketPriceResponse();
        if (!ObjectId.isValid(idCate)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id typeNews không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(MarketPrice.class.getSimpleName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{$and: [{idCate: #}]}");
        MongoCursor<MarketPrice> cursor = collection.find(stringBuilder.toString(),idCate).sort("{timeCreate:-1}").skip(page*ofset).limit(ofset).as(MarketPrice.class);
        JsonArray array = new JsonArray();
        response.setTotal(cursor.count());
        while (cursor.hasNext()){
            array.add(cursor.next().toJson());
        }
        response.setArray(array);
        return response;
    }

    @Override
    public MarketPriceResponse editMarketPrice(String id,String idCate, String name, String price, String place, String unit, String note) throws Exception {
        MarketPriceResponse response = new MarketPriceResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        long pr = 0;
        if(price!=null){
            try{
                pr = Long.parseLong(price);
            }catch (Exception e){
                e.printStackTrace();
                response.setError(ErrorCode.CANT_CAST_TYPE);
                response.setMsg("Lĩ ép kiểu.");
                return response;
            }
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(MarketPrice.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<MarketPrice> cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(MarketPrice.class);
        if(cursor.hasNext()){
            MarketPrice marketPrice = cursor.next();
            if(idCate!=null){
                if (!ObjectId.isValid(idCate)) {
                    response.setError(ErrorCode.NOT_A_OBJECT_ID);
                    response.setMsg("Id danh mục tin tức không đúng.");
                    return response;
                }
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{idCate:#}}",idCate);
                marketPrice.setNote(idCate);
            }
            if(name!=null){
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{name:#}}",name);
                marketPrice.setName(name);
            }
            if(price!=null){
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{price:#}}",pr);
                marketPrice.setPrice(pr);
            }
            if(place!=null){
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{place:#}}",place);
                marketPrice.setPlace(place);
            }
            if(unit!=null){
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{unit:#}}",unit);
                marketPrice.setUnit(unit);
            }

            if(note!=null){
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{note:#}}",note);
                marketPrice.setNote(note);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);

            marketPrice.setTimeCreate(calendar.getTimeInMillis());
            collection.update("{_id:#}", new ObjectId(id)).with("{$set:{timeCreate:#}}",marketPrice.getTimeCreate());
            response.setData(marketPrice.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
            return response;
        }

        return  response;
    }
}
