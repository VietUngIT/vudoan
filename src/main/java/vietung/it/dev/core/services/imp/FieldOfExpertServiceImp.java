package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.FieldOfExpertResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.Expert;
import vietung.it.dev.core.models.FieldOfExpert;
import vietung.it.dev.core.services.FieldOfExpertService;
import vietung.it.dev.core.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class FieldOfExpertServiceImp implements FieldOfExpertService {
    @Override
    public FieldOfExpertResponse getAllField() {
        FieldOfExpertResponse response = new FieldOfExpertResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(FieldOfExpert.class.getSimpleName());
        MongoCursor<FieldOfExpert> cursor = collection.find().as(FieldOfExpert.class);
        JsonArray jsonArray = new JsonArray();
        while (cursor.hasNext()){
            FieldOfExpert fieldOfExpert = cursor.next();
            jsonArray.add(fieldOfExpert.toJson());
        }
        response.setArray(jsonArray);
        return response;
    }

    @Override
    public FieldOfExpertResponse getFieldByID(String id) {
        FieldOfExpertResponse response = new FieldOfExpertResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(FieldOfExpert.class.getSimpleName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{_id: #}");
        MongoCursor<FieldOfExpert> cursor = collection.find(stringBuilder.toString(),new ObjectId(id)).limit(1).as(FieldOfExpert.class);
        if (cursor.hasNext()){
            FieldOfExpert fieldOfExpert = cursor.next();
            response.setData(fieldOfExpert.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("ID không tồn tại.");
        }
        return response;
    }

    @Override
    public FieldOfExpertResponse getByIdParentField(String id) throws Exception {
        FieldOfExpertResponse response = new FieldOfExpertResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(FieldOfExpert.class.getSimpleName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{idParentField: #}");
        MongoCursor<FieldOfExpert> cursor = collection.find(stringBuilder.toString(),id).as(FieldOfExpert.class);
        JsonArray jsonArray = new JsonArray();
        while (cursor.hasNext()){
            FieldOfExpert fieldOfExpert = cursor.next();
            jsonArray.add(fieldOfExpert.toJson());
        }
        response.setArray(jsonArray);
        return response;
    }

    @Override
    public FieldOfExpertResponse addFieldOfExpert(String nameField, String tags, String idParentField) throws Exception{
        FieldOfExpertResponse response = new FieldOfExpertResponse();
        FieldOfExpert fieldOfExpert = new FieldOfExpert();
        if (!ObjectId.isValid(idParentField) && !idParentField.equals("")) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("idParentField không đúng.");
            return response;
        }
        ObjectId objectId = new ObjectId();
        fieldOfExpert.set_id(objectId.toHexString());
        fieldOfExpert.setIdParentField(idParentField);
        fieldOfExpert.setNameField(nameField);
        JsonArray arrayTags = Utils.toJsonArray(tags);
        List<String> lsttags = new ArrayList<>();
        for (int i=0;i<arrayTags.size();i++){
            lsttags.add(arrayTags.get(i).getAsString().toLowerCase());
        }
        fieldOfExpert.setTags(lsttags);
        MongoPool.log(FieldOfExpert.class.getSimpleName(),fieldOfExpert.toDocument());
        response.setData(fieldOfExpert.toJson());
        return response;
    }

    @Override
    public FieldOfExpertResponse editFieldOfExpert(String id, String nameField,String tags, String idParentField) throws Exception {
        FieldOfExpertResponse response = new FieldOfExpertResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        if (!ObjectId.isValid(idParentField)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("idParentField không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(FieldOfExpert.class.getSimpleName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{_id: #}");
        MongoCursor<FieldOfExpert> cursor = collection.find(stringBuilder.toString(),new ObjectId(id)).limit(1).as(FieldOfExpert.class);
        if (cursor.hasNext()){
            FieldOfExpert fieldOfExpert = cursor.next();
            JsonArray arrayTags = Utils.toJsonArray(tags);
            List<String> lsttags = new ArrayList<>();
            for (int i=0;i<arrayTags.size();i++){
                lsttags.add(arrayTags.get(i).getAsString().toLowerCase());
            }
            collection.update("{_id: #}",new ObjectId(id)).with("{$set: {idParentField: #,nameField: #,tags: #}}",idParentField,nameField,lsttags);
            fieldOfExpert.setIdParentField(idParentField);
            fieldOfExpert.setNameField(nameField);
            fieldOfExpert.setTags(lsttags);
            response.setData(fieldOfExpert.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("ID không tồn tại.");
        }
        return response;
    }

    @Override
    public FieldOfExpertResponse deleteFieldOfExpert(String id) {
        FieldOfExpertResponse response = new FieldOfExpertResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(FieldOfExpert.class.getSimpleName());
        MongoCollection collectionExpert = jongo.getCollection(Expert.class.getSimpleName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{_id: #}");
        MongoCursor<FieldOfExpert> cursor = collection.find(stringBuilder.toString(),new ObjectId(id)).limit(1).as(FieldOfExpert.class);
        if (cursor.hasNext()){
            FieldOfExpert fieldOfExpert = cursor.next();
            collection.remove(new ObjectId(fieldOfExpert.get_id()));
            collectionExpert.update("{}").multi().with("{ $pull: {idFields:#}}",id);
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("ID không tồn tại.");
        }
        return response;
    }

}
