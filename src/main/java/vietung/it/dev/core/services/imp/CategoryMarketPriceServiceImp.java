package vietung.it.dev.core.services.imp;


import com.google.gson.JsonArray;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.CategoryMarketPriceResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.CategoryMarketPrice;
import vietung.it.dev.core.services.CategoryMarketPriceService;
import vietung.it.dev.core.services.UploadService;

public class CategoryMarketPriceServiceImp implements CategoryMarketPriceService {
    @Override
    public CategoryMarketPriceResponse getAllCategoryMarketPrice() throws Exception {
        CategoryMarketPriceResponse response = new CategoryMarketPriceResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(CategoryMarketPrice.class.getSimpleName());
        MongoCursor<CategoryMarketPrice> cursor = collection.find().as(CategoryMarketPrice.class);
        JsonArray array = new JsonArray();
        while(cursor.hasNext()){
            CategoryMarketPrice cate = cursor.next();
            array.add(cate.toJson());
        }
        response.setArray(array);
        return response;
    }

    @Override
    public CategoryMarketPriceResponse getCategoryMarketPriceById(String id) throws Exception {
        CategoryMarketPriceResponse response = new CategoryMarketPriceResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(CategoryMarketPrice.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<CategoryMarketPrice> cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(CategoryMarketPrice.class);
        if(cursor.hasNext()){
            CategoryMarketPrice cate = cursor.next();
            response.setData(cate.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }
        return response;
    }

    @Override
    public CategoryMarketPriceResponse addCategoryMarketPrice(String name, String image) throws Exception {
        CategoryMarketPriceResponse response = new CategoryMarketPriceResponse();
        UploadService service = new UploadServiceImp();
        try{
            String urlImage = null;
            if(image!=null){
                urlImage = service.uploadImage(image);
            }
            CategoryMarketPrice category = new CategoryMarketPrice();
            ObjectId _id = new ObjectId();
            category.set_id(_id.toHexString());
            category.setName(name);
            category.setImage(urlImage);
            MongoPool.log(CategoryMarketPrice.class.getSimpleName(),category.toDocument());
            response.setData(category.toJson());
        }catch (Exception e){
            e.printStackTrace();
            response.setError(ErrorCode.UPLOAD_IMAGE_ERROR);
            response.setMsg("Upload ảnh lỗi.nguyên nhân có thể do dữ liệu ảnh gửi lên bị sai.");
        }

        return response;
    }

    @Override
    public CategoryMarketPriceResponse editNameCategoryMarketPrice(String id, String name) throws Exception {
        CategoryMarketPriceResponse response = new CategoryMarketPriceResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(CategoryMarketPrice.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<CategoryMarketPrice> cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(CategoryMarketPrice.class);
        if(cursor.hasNext()){
            CategoryMarketPrice category = cursor.next();
            category.setName(name);
            collection.update("{_id:#}", new ObjectId(id)).with("{$set:{name:#}}",name);
            response.setData(category.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
            return response;
        }
        return response;
    }

    @Override
    public CategoryMarketPriceResponse editImageCategoryMarketPrice(String id, String image) throws Exception {
        CategoryMarketPriceResponse response = new CategoryMarketPriceResponse();
        UploadService service = new UploadServiceImp();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        try {
            String urlImage = service.uploadImage(image);
            DB db = MongoPool.getDBJongo();
            Jongo jongo = new Jongo(db);
            MongoCollection collection = jongo.getCollection(CategoryMarketPrice.class.getSimpleName());
            StringBuilder builder = new StringBuilder();
            builder.append("{_id:#}");
            MongoCursor<CategoryMarketPrice> cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(CategoryMarketPrice.class);
            if(cursor.hasNext()){
                CategoryMarketPrice category = cursor.next();
                category.setImage(urlImage);
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{image:#}}",urlImage);
                response.setData(category.toJson());
            }else {
                response.setError(ErrorCode.ID_NOT_EXIST);
                response.setMsg("Id không tồn tại.");
            }
        }catch (Exception e){
            e.printStackTrace();
            response.setError(ErrorCode.UPLOAD_IMAGE_ERROR);
            response.setMsg("Upload ảnh lỗi.nguyên nhân có thể do dữ liệu ảnh gửi lên bị sai.");
        }

        return response;
    }

    @Override
    public CategoryMarketPriceResponse deleteCategoryMarketPrice(String id) throws Exception {
        CategoryMarketPriceResponse response = new CategoryMarketPriceResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(CategoryMarketPrice.class.getSimpleName());
        collection.remove(new ObjectId(id));

        return response;
    }
}
