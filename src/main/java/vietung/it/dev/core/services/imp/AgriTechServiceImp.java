package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.AgriTechResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.consts.Variable;
import vietung.it.dev.core.models.AgriTech;
import vietung.it.dev.core.models.SubCategory;
import vietung.it.dev.core.services.AgriTechService;
import vietung.it.dev.core.services.UploadService;
import vietung.it.dev.core.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AgriTechServiceImp implements AgriTechService {
    @Override
    public AgriTechResponse deleteNewsAgriTech(String id) throws Exception{
        AgriTechResponse response = new AgriTechResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(AgriTech.class.getSimpleName());
        collection.remove(new ObjectId(id));
        return response;
    }

    @Override
    public AgriTechResponse editTagsNewsAgriTech(String id, String tags) throws Exception {
        AgriTechResponse response = new AgriTechResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id tin tức không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(AgriTech.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<AgriTech> cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(AgriTech.class);
        if(cursor.hasNext()){
            AgriTech agriTech = cursor.next();
            JsonArray array = Utils.toJsonArray(tags);
            List<String> lstTag = new ArrayList<>();
            for (int i=0;i<array.size();i++){
                lstTag.add(array.get(i).getAsString());
            }
            collection.update("{_id:#}", new ObjectId(id)).with("{$set:{tags:#}}",lstTag);
            agriTech.setTags(lstTag);

            MongoCollection collectionIdCate = jongo.getCollection(Variable.MG_SUB_CATEGORY_AGRI_TECH);
            MongoCursor<SubCategory> cursorCate = collectionIdCate.find("{_id:#}", new ObjectId(agriTech.getIdSubCate())).as(SubCategory.class);
            if(cursorCate.hasNext()){
                agriTech.setNameSubCate(cursorCate.next().getNameSubCate());
            }
            response.setData(agriTech.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
            return response;
        }
        return response;
    }

    @Override
    public AgriTechResponse editImageNewsAgriTech(String id, String image) throws Exception {
        AgriTechResponse response = new AgriTechResponse();
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
            MongoCollection collection = jongo.getCollection(AgriTech.class.getSimpleName());
            StringBuilder builder = new StringBuilder();
            builder.append("{_id:#}");
            MongoCursor<AgriTech> cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(AgriTech.class);
            if(cursor.hasNext()){
                AgriTech agriTech = cursor.next();
                agriTech.setImage(urlImage);
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{image:#}}",agriTech.getImage());
                JsonObject object = new JsonObject();
                object.addProperty("image",agriTech.getImage());
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
    public AgriTechResponse editNewsAgriTech(String id, String title, String author, String idSubCate, String content) throws Exception {
        AgriTechResponse response = new AgriTechResponse();

        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id tin tức không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(AgriTech.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<AgriTech> cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(AgriTech.class);
        if(cursor.hasNext()){
            AgriTech agriTech = cursor.next();

            if(title!=null){
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{title:#}}",title);
                agriTech.setTitle(title);
            }
            if(author!=null){
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{author:#}}",author);
                agriTech.setAuthor(author);
            }
            if(idSubCate!=null){
                if (!ObjectId.isValid(idSubCate)) {
                    response.setError(ErrorCode.NOT_A_OBJECT_ID);
                    response.setMsg("Id danh mục kỹ thuật không đúng.");
                    return response;
                }
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{idSubCate:#}}",idSubCate);
                agriTech.setIdSubCate(idSubCate);
            }
            if(content!=null){
                collection.update("{_id:#}", new ObjectId(id)).with("{$set:{content:#}}",content);
                agriTech.setContent(content);
            }

            MongoCollection collectionIdCate = jongo.getCollection(Variable.MG_SUB_CATEGORY_AGRI_TECH);
            MongoCursor<SubCategory> cursorCate = collectionIdCate.find("{_id:#}", new ObjectId(agriTech.getIdSubCate())).as(SubCategory.class);
            if(cursorCate.hasNext()){
                agriTech.setNameSubCate(cursorCate.next().getNameSubCate());
            }
            response.setData(agriTech.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
            return response;
        }
        return response;
    }

    @Override
    public AgriTechResponse createNewsAgriTech(String title, String author, String image, String tags, String idSubCate, String content) throws Exception{
        AgriTechResponse response = new AgriTechResponse();
        UploadService service = new UploadServiceImp();
        if (!ObjectId.isValid(idSubCate)) {
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
                lstTag.add(array.get(i).getAsString());
            }
            AgriTech agriTech = new AgriTech();
            ObjectId _id = new ObjectId();
            agriTech.set_id(_id.toHexString());
            agriTech.setIdSubCate(idSubCate);
            agriTech.setImage(urlImage);
            agriTech.setTitle(title);
            agriTech.setTimeCreate(Calendar.getInstance().getTimeInMillis());
            agriTech.setAuthor(author);
            agriTech.setNumComment(0);
            agriTech.setTags(lstTag);
            agriTech.setContent(content);
            MongoPool.log(AgriTech.class.getSimpleName(),agriTech.toDocument());

            MongoCollection collectionIdCate = jongo.getCollection(Variable.MG_SUB_CATEGORY_AGRI_TECH);
            MongoCursor<SubCategory> cursorCate = collectionIdCate.find("{_id:#}", new ObjectId(idSubCate)).as(SubCategory.class);
            if(cursorCate.hasNext()){
                agriTech.setNameSubCate(cursorCate.next().getNameSubCate());
            }
            response.setData(agriTech.toJson());

        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public AgriTechResponse getNewsAgriTechById(String id) throws Exception {
        AgriTechResponse response = new AgriTechResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(AgriTech.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<AgriTech> cursor = collection.find(builder.toString(), new ObjectId(id)).as(AgriTech.class);
        if(cursor.hasNext()){
            AgriTech marketInfo = cursor.next();
            response.setData(marketInfo.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }

        return response;
    }

    @Override
    public AgriTechResponse getAllNewsAgriTechBySubCate(int page, int ofs, String idsubcate) throws Exception {
        AgriTechResponse response = new AgriTechResponse();
        if (!ObjectId.isValid(idsubcate)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<AgriTech> cursor = null;
        MongoCollection collection = jongo.getCollection(AgriTech.class.getSimpleName());
        builder.append("{$and: [{idSubCate: #}]}");
        cursor = collection.find(builder.toString(),idsubcate).sort("{timeCreate:-1}").skip(page*ofs).limit(ofs).as(AgriTech.class);
        JsonArray jsonArray = new JsonArray();
        while(cursor.hasNext()){
            AgriTech agriTech = cursor.next();
            jsonArray.add(agriTech.toJson());
        }
        response.setArray(jsonArray);

        return response;
    }
}
