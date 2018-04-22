package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.ParentFieldExpertResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.ParentFieldExpert;
import vietung.it.dev.core.services.ParentFieldExpertService;
import vietung.it.dev.core.utils.Utils;

public class ParentFieldExpertServiceImp implements ParentFieldExpertService {
    @Override
    public ParentFieldExpertResponse getAllParentField() throws Exception {
        ParentFieldExpertResponse response = new ParentFieldExpertResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(ParentFieldExpert.class.getSimpleName());
        MongoCursor<ParentFieldExpert> cursor = collection.find().as(ParentFieldExpert.class);
        JsonArray jsonArray = new JsonArray();
        while (cursor.hasNext()){
            ParentFieldExpert parentFieldExpert = cursor.next();
            jsonArray.add(parentFieldExpert.toJson());
        }
        response.setArray(jsonArray);
        return response;
    }

    @Override
    public ParentFieldExpertResponse getParentFieldByID(String id) throws Exception {
        ParentFieldExpertResponse response = new ParentFieldExpertResponse();
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(ParentFieldExpert.class.getSimpleName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{_id: #}");
        MongoCursor<ParentFieldExpert> cursor = collection.find(stringBuilder.toString(),new ObjectId(id)).limit(1).as(ParentFieldExpert.class);
        if (cursor.hasNext()){
            ParentFieldExpert parentFieldExpert = cursor.next();
            response.setData(parentFieldExpert.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("ID không tồn tại.");
        }
        return response;
    }

    @Override
    public ParentFieldExpertResponse addParentFieldExpert(String nameField) {
        ParentFieldExpertResponse response = new ParentFieldExpertResponse();
        ParentFieldExpert parentFieldExpert = new ParentFieldExpert();
        ObjectId objectId = new ObjectId();
        parentFieldExpert.set_id(objectId.toHexString());
        parentFieldExpert.setName(nameField);
        MongoPool.log(ParentFieldExpert.class.getSimpleName(),parentFieldExpert.toDocument());
        response.setData(parentFieldExpert.toJson());
        return response;
    }

    @Override
    public ParentFieldExpertResponse editParentFieldExpert(String id, String nameField) {
        ParentFieldExpertResponse response = new ParentFieldExpertResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(ParentFieldExpert.class.getSimpleName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{_id: #}");
        MongoCursor<ParentFieldExpert> cursor = collection.find(stringBuilder.toString(),new ObjectId(id)).limit(1).as(ParentFieldExpert.class);
        if (cursor.hasNext()){
            ParentFieldExpert parentFieldExpert = cursor.next();
            collection.update("{_id: #}",new ObjectId(id)).with("{$set: {name: #}}",nameField);
            parentFieldExpert.setName(nameField);
            response.setData(parentFieldExpert.toJson());
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("ID không tồn tại.");
        }
        return response;
    }

    @Override
    public ParentFieldExpertResponse deleteParentFieldExpert(String id) {
        ParentFieldExpertResponse response = new ParentFieldExpertResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(ParentFieldExpert.class.getSimpleName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{_id: #}");
        MongoCursor<ParentFieldExpert> cursor = collection.find(stringBuilder.toString(),new ObjectId(id)).limit(1).as(ParentFieldExpert.class);
        if (cursor.hasNext()){
            ParentFieldExpert parentFieldExpert = cursor.next();
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("ID không tồn tại.");
        }
        return response;
    }
}
