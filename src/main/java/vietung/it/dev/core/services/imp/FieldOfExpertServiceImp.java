package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.CategoryResponse;
import vietung.it.dev.apis.response.FieldOfExpertResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.consts.Variable;
import vietung.it.dev.core.models.Category;
import vietung.it.dev.core.models.Expert;
import vietung.it.dev.core.models.FieldOfExpert;
import vietung.it.dev.core.models.News;
import vietung.it.dev.core.services.FieldOfExpertService;
import vietung.it.dev.core.utils.Utils;

public class FieldOfExpertServiceImp implements FieldOfExpertService {
    @Override
    public CategoryResponse getAllField() {
        CategoryResponse response = new CategoryResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Variable.MG_CATEGORY_FIELD_EXPERT);
        MongoCursor<Category> cursor = collection.find().as(Category.class);
        JsonArray jsonArray = new JsonArray();
        while (cursor.hasNext()){
            Category category = cursor.next();
            jsonArray.add(category.toJson());
        }
        response.setArray(jsonArray);
        return response;
    }

    @Override
    public CategoryResponse getFieldByID(String id) {
        CategoryResponse response = new CategoryResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Variable.MG_CATEGORY_FIELD_EXPERT);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{_id: #}");
        MongoCursor<Category> cursor = collection.find(stringBuilder.toString(),new ObjectId(id)).limit(1).as(Category.class);
        if (cursor.hasNext()){
            Category category = cursor.next();
            response.setData(category.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("ID không tồn tại.");
        }
        return response;
    }

    @Override
    public CategoryResponse addFieldOfExpert(String nameField) throws Exception{
        CategoryResponse response = new CategoryResponse();
        Category category = new Category();
        ObjectId objectId = new ObjectId();
        category.set_id(objectId.toHexString());
        category.setName(nameField);
        MongoPool.log(Variable.MG_CATEGORY_FIELD_EXPERT,category.toDocument());
        response.setData(category.toJson());
        return response;
    }

    @Override
    public CategoryResponse editFieldOfExpert(String id, String nameField) throws Exception {
        CategoryResponse response = new CategoryResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Variable.MG_CATEGORY_FIELD_EXPERT);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{_id: #}");
        MongoCursor<Category> cursor = collection.find(stringBuilder.toString(),new ObjectId(id)).limit(1).as(Category.class);
        if (cursor.hasNext()){
            Category category = cursor.next();
            collection.update("{_id: #}",new ObjectId(id)).with("{$set: {name: #}}",nameField);
            category.setName(nameField);
            response.setData(category.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("ID không tồn tại.");
        }
        return response;
    }

    @Override
    public CategoryResponse deleteFieldOfExpert(String id) {
        CategoryResponse response = new CategoryResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Variable.MG_CATEGORY_FIELD_EXPERT);
        MongoCollection collectionExpert = jongo.getCollection(Expert.class.getSimpleName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{_id: #}");
        MongoCursor<Category> cursor = collection.find(stringBuilder.toString(),new ObjectId(id)).limit(1).as(Category.class);
        if (cursor.hasNext()){
            Category category = cursor.next();
            collection.remove(new ObjectId(category.get_id()));
            collectionExpert.update("{}").multi().with("{ $pull: {idFields:#}}",id);
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("ID không tồn tại.");
        }
        return response;
    }

}
