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
import vietung.it.dev.core.models.FieldOfExpert;
import vietung.it.dev.core.services.FieldOfExpertService;
import vietung.it.dev.core.utils.Utils;

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
        response.setListFieldOfExpert(jsonArray);
        return response;
    }

    @Override
    public FieldOfExpertResponse getFieldByID(int id) {
        FieldOfExpertResponse response = new FieldOfExpertResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(FieldOfExpert.class.getSimpleName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{idField: #}");
        MongoCursor<FieldOfExpert> cursor = collection.find(stringBuilder.toString(),id).limit(1).as(FieldOfExpert.class);
        if (cursor.hasNext()){
            FieldOfExpert fieldOfExpert = cursor.next();
            response.setFieldOfExpert(fieldOfExpert);
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("ID không tồn tại.");
        }
        return response;
    }

    @Override
    public FieldOfExpertResponse addFieldOfExpert(String nameField) {
        FieldOfExpertResponse response = new FieldOfExpertResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(FieldOfExpert.class.getSimpleName());
        MongoCursor<FieldOfExpert> cursor = collection.find().sort("idField:-1").limit(1).as(FieldOfExpert.class);
        int idNext = 1;
        if (cursor.hasNext()){
            FieldOfExpert fieldOfExpert = cursor.next();
            idNext = fieldOfExpert.getIdField();
        }
        FieldOfExpert fieldOfExpert = new FieldOfExpert();
        fieldOfExpert.setIdField(idNext);
        fieldOfExpert.setNameField(nameField);
        MongoPool.log(FieldOfExpert.class.getSimpleName(),fieldOfExpert.toDocument());
        response.setFieldOfExpert(fieldOfExpert);
        return response;
    }

    @Override
    public FieldOfExpertResponse editFieldOfExpert(int id, String nameField) {
        FieldOfExpertResponse response = new FieldOfExpertResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(FieldOfExpert.class.getSimpleName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{idField: #}");
        MongoCursor<FieldOfExpert> cursor = collection.find(stringBuilder.toString(),id).limit(1).as(FieldOfExpert.class);
        if (cursor.hasNext()){
            FieldOfExpert fieldOfExpert = new FieldOfExpert();
            collection.update("{idField: #}",id).with("{$set: {nameField: #}}",nameField);
            fieldOfExpert.setIdField(id);
            fieldOfExpert.setNameField(nameField);
            response.setFieldOfExpert(fieldOfExpert);
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("ID không tồn tại.");
        }
        return response;
    }

    @Override
    public FieldOfExpertResponse deleteFieldOfExpert(int id) {
        FieldOfExpertResponse response = new FieldOfExpertResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(FieldOfExpert.class.getSimpleName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{idField: #}");
        MongoCursor<FieldOfExpert> cursor = collection.find(stringBuilder.toString(),id).limit(1).as(FieldOfExpert.class);
        if (cursor.hasNext()){
            FieldOfExpert fieldOfExpert = cursor.next();
            collection.remove(new ObjectId(fieldOfExpert.get_id()));
            response.setFieldOfExpert(fieldOfExpert);
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("ID không tồn tại.");
        }
        return response;
    }
}
