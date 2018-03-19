package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.CategoryResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.Category;
import vietung.it.dev.core.services.CategoryService;
import vietung.it.dev.core.utils.Utils;

public class CategoryServiceImp implements CategoryService {
    public static final String MG_CATEGORY_NEWS = "CategoryNews";
    @Override
    public CategoryResponse getAllCategoryNews() throws Exception {
        CategoryResponse response = new CategoryResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(MG_CATEGORY_NEWS);
        MongoCursor<Category> cursor = collection.find().as(Category.class);
        JsonArray array = new JsonArray();
        while(cursor.hasNext()){
            Category cate = cursor.next();
            array.add(cate.toJson());
        }
        response.setArray(array);
        return response;
    }

    @Override
    public CategoryResponse getCategoryNewsById(String id) throws Exception {
        CategoryResponse response = new CategoryResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(MG_CATEGORY_NEWS);
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<Category> cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(Category.class);
        if(cursor.hasNext()){
            Category cate = cursor.next();
            response.setData(cate.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }
        return response;
    }

    @Override
    public CategoryResponse addCategoryNews(String name) throws Exception {
        CategoryResponse response = new CategoryResponse();
        Category category = new Category();
        ObjectId _id = new ObjectId();
        category.set_id(_id.toHexString());
        category.setName(name);
        MongoPool.log(MG_CATEGORY_NEWS,category.toDocument());
        response.setData(category.toJson());
        return response;
    }

    @Override
    public CategoryResponse editCategoryNews(String id, String name) throws Exception {
        CategoryResponse response = new CategoryResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(MG_CATEGORY_NEWS);
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<Category> cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(Category.class);
        if(cursor.hasNext()){
            Category category = cursor.next();
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
    public CategoryResponse deleteCategoryNews(String id) throws Exception {
        CategoryResponse response = new CategoryResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(MG_CATEGORY_NEWS);
        collection.remove(new ObjectId(id));

        return response;
    }
}
