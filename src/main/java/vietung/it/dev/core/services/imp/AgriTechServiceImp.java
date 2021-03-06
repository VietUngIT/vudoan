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
import vietung.it.dev.core.models.Category;
import vietung.it.dev.core.models.SubCategory;
import vietung.it.dev.core.services.AgriTechService;
import vietung.it.dev.core.services.UploadService;
import vietung.it.dev.core.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AgriTechServiceImp implements AgriTechService {
    @Override
    public int commentNews(String idNews, Boolean isCmt) {
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(AgriTech.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        MongoCursor<AgriTech> cursor = collection.find(builder.toString(), new ObjectId(idNews)).as(AgriTech.class);
        if(cursor.hasNext()){
            AgriTech agriTech = cursor.next();
            int cmt = agriTech.getNumComment()>=0?agriTech.getNumComment():0;
            if(isCmt){
                agriTech.setNumComment(cmt+1);
            }else {
                agriTech.setNumComment(cmt-1);
            }

            collection.update("{_id:#}", new ObjectId(idNews)).with("{$set:{numComment:#}}",agriTech.getNumComment());
            return agriTech.getNumComment();
        }
        return 0;
    }

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
                lstTag.add(array.get(i).getAsString().toLowerCase());
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
            System.out.println("image: "+image);
            if(!(image.equals("null") || image==null)){
                urlImage = service.uploadImage(image);
            }
            JsonArray array = Utils.toJsonArray(tags);
            List<String> lstTag = new ArrayList<>();
            for (int i=0;i<array.size();i++){
                lstTag.add(array.get(i).getAsString().toLowerCase());
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
            response.setError(ErrorCode.UPLOAD_IMAGE_ERROR);
            response.setMsg("Lỗi cập nhật ảnh.");
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
        response.setTotal(cursor.count());
        while(cursor.hasNext()){
            AgriTech agriTech = cursor.next();
            jsonArray.add(agriTech.toJson());
        }
        response.setArray(jsonArray);

        return response;
    }

    @Override
    public AgriTechResponse getHomeForAgriTech() throws Exception {
        AgriTechResponse response = new AgriTechResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Variable.MG_CATEGORY_AGRI_TECH);
        MongoCursor<Category> cursor = collection.find().as(Category.class);
        JsonArray array = new JsonArray();
        while(cursor.hasNext()){
            Category cate = cursor.next();
            JsonObject object = new JsonObject();
            object.addProperty("idCate",cate.get_id());
            object.addProperty("nameCate",cate.getName());
            object.add("subCate",getAllSubCateAgriTechByIdCate(cate.get_id(),jongo));
            array.add(object);
        }
        response.setArray(array);
        return response;
    }

    private JsonArray getAllSubCateAgriTechByIdCate(String idCate, Jongo jongo){
        MongoCollection collection = jongo.getCollection(Variable.MG_SUB_CATEGORY_AGRI_TECH);
        StringBuilder builder = new StringBuilder();
        builder.append("{idCate:#}");
        MongoCursor<SubCategory> cursor = collection.find(builder.toString(),idCate).as(SubCategory.class);
        JsonArray array = new JsonArray();
        int i=0;
        while(cursor.hasNext()){
            SubCategory subCategory = cursor.next();
            JsonObject object = new JsonObject();
            object.addProperty("idSubCate",subCategory.get_id());
            object.addProperty("nameSubCate",subCategory.getNameSubCate());
            if(i==0){
                object.add("news",getThreeNewsAgriTechForSubCate(subCategory.get_id(),jongo));
            }
            i++;
            array.add(object);
        }
        return array;
    }

    private JsonArray getThreeNewsAgriTechForSubCate(String idSubCate,Jongo jongo){
        StringBuilder builder = new StringBuilder();
        MongoCursor<AgriTech> cursor = null;
        MongoCollection collection = jongo.getCollection(AgriTech.class.getSimpleName());
        builder.append("{$and: [{idSubCate: #}]}");
        cursor = collection.find(builder.toString(),idSubCate).sort("{timeCreate:-1}").limit(3).as(AgriTech.class);
        JsonArray jsonArray = new JsonArray();
        while(cursor.hasNext()){
            AgriTech agriTech = cursor.next();
            jsonArray.add(agriTech.toJson());
        }
        return jsonArray;
    }
}
