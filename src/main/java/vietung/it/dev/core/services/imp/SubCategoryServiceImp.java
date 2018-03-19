package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.SubCategoryResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.consts.Variable;
import vietung.it.dev.core.models.Category;
import vietung.it.dev.core.models.SubCategory;
import vietung.it.dev.core.services.SubCategoryService;

public class SubCategoryServiceImp implements SubCategoryService {

    @Override
    public SubCategoryResponse getAllSubCategoryAgritech() throws Exception {
        SubCategoryResponse response = new SubCategoryResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Variable.MG_SUB_CATEGORY_AGRI_TECH);
        MongoCollection collectionCate = jongo.getCollection(Variable.MG_CATEGORY_AGRI_TECH);
        MongoCursor<SubCategory> cursor = collection.find().as(SubCategory.class);
        JsonArray array = new JsonArray();
        while(cursor.hasNext()){
            SubCategory subCategory = cursor.next();
            MongoCursor<Category> cursorCate = collectionCate.find("_id:#", subCategory.getIdCate()).as(Category.class);
            if(cursorCate.hasNext()){
                subCategory.setNameCate(cursorCate.next().getName());
            }
            array.add(subCategory.toJson());
        }
        response.setArray(array);
        return response;
    }

    @Override
    public SubCategoryResponse getSubCategoryAgriTechByCate(String id) throws Exception {
        SubCategoryResponse response = new SubCategoryResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Variable.MG_SUB_CATEGORY_AGRI_TECH);
        MongoCollection collectionCate = jongo.getCollection(Variable.MG_CATEGORY_AGRI_TECH);
        StringBuilder builder = new StringBuilder();
        builder.append("{idCate:#}");
        MongoCursor<SubCategory> cursor = collection.find(builder.toString(),id).as(SubCategory.class);
        String namecate = "";
        MongoCursor<Category> cursorCate = collectionCate.find("_id:#", new ObjectId(id)).as(Category.class);
        if(cursorCate.hasNext()){
            namecate = cursorCate.next().getName();
        }
        while(cursor.hasNext()){
            SubCategory subCategory = cursor.next();
            subCategory.setNameCate(namecate);
            response.setData(subCategory.toJson());
        }
        return response;
    }

    @Override
    public SubCategoryResponse getSubCategoryAgritechById(String id) throws Exception {
        SubCategoryResponse response = new SubCategoryResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Variable.MG_SUB_CATEGORY_AGRI_TECH);
        MongoCollection collectionCate = jongo.getCollection(Variable.MG_CATEGORY_AGRI_TECH);
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<SubCategory> cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(SubCategory.class);
        if(cursor.hasNext()){
            SubCategory subCategory = cursor.next();
            MongoCursor<Category> cursorCate = collectionCate.find("_id:#", subCategory.getIdCate()).as(Category.class);
            if(cursorCate.hasNext()){
                subCategory.setNameCate(cursorCate.next().getName());
            }
            response.setData(subCategory.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }
        return response;
    }

    @Override
    public SubCategoryResponse addSubCategoryAgritech(String idcate, String name) throws Exception {
        SubCategoryResponse response = new SubCategoryResponse();
        if (!ObjectId.isValid(idcate)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        SubCategory subCategory = new SubCategory();
        ObjectId _id = new ObjectId();
        subCategory.set_id(_id.toHexString());
        subCategory.setIdCate(idcate);
        subCategory.setNameSubCate(name);
        MongoPool.log(Variable.MG_SUB_CATEGORY_AGRI_TECH,subCategory.toDocument());
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collectionCate = jongo.getCollection(Variable.MG_CATEGORY_AGRI_TECH);
        MongoCursor<Category> cursorCate = collectionCate.find("_id:#", subCategory.getIdCate()).as(Category.class);
        if(cursorCate.hasNext()){
            subCategory.setNameCate(cursorCate.next().getName());
        }
        response.setData(subCategory.toJson());
        return response;
    }

    @Override
    public SubCategoryResponse editSubCategoryAgritech(String id, String name) throws Exception {
        SubCategoryResponse response = new SubCategoryResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Variable.MG_SUB_CATEGORY_AGRI_TECH);
        MongoCollection collectionCate = jongo.getCollection(Variable.MG_CATEGORY_AGRI_TECH);
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<SubCategory> cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(SubCategory.class);
        if(cursor.hasNext()){
            SubCategory subCategory = cursor.next();
            subCategory.setNameSubCate(name);
            collection.update("{_id:#}", new ObjectId(id)).with("{$set:{nameSubCate:#}}",name);
            MongoCursor<Category> cursorCate = collectionCate.find("_id:#", subCategory.getIdCate()).as(Category.class);
            if(cursorCate.hasNext()){
                subCategory.setNameCate(cursorCate.next().getName());
            }
            response.setData(subCategory.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
            return response;
        }
        return response;
    }

    @Override
    public SubCategoryResponse deleteSubCategoryAgritech(String id) throws Exception {
        SubCategoryResponse response = new SubCategoryResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Variable.MG_SUB_CATEGORY_AGRI_TECH);
        collection.remove(new ObjectId(id));

        return response;
    }
}
