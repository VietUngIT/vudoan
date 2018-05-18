package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.MessagesResponse;
import vietung.it.dev.apis.response.NotificationResponse;
import vietung.it.dev.core.config.Chat;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.*;
import vietung.it.dev.core.services.NotificationService;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class NotificationServiceImp implements NotificationService {
    @Override
    public NotificationResponse getAllNotificationByIdReceiver(int page, int ofset, String idreceiver) {
        NotificationResponse response = new NotificationResponse();
        if (!ObjectId.isValid(idreceiver)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<Notification> cursor = null;
        MongoCollection collection = jongo.getCollection(Notification.class.getSimpleName());
        builder.append("{$and: [{idReceiver: #}]}");
        cursor = collection.find(builder.toString(),idreceiver).sort("{create_at:-1}").skip(page).limit(ofset).as(Notification.class);
        JsonArray jsonArray = new JsonArray();
        while(cursor.hasNext()){
            Notification notification = cursor.next();
            MongoCollection collectionUser = jongo.getCollection(Users.class.getSimpleName());
            StringBuilder sb = new StringBuilder();
            sb.append("{$and: [{_id: #}]}");
            MongoCursor<Users> cursorUser = collectionUser.find(sb.toString(), notification.getIdSend()).limit(1).as(Users.class);
            if (cursorUser.hasNext()){
                Users users = cursorUser.next();
                notification.setNameSend(users.getName());
                if(users.getAvatar() != null){
                    notification.setAvataSend(users.getAvatar());
                }else {
                    notification.setAvataSend("");
                }
            }
            jsonArray.add(notification.toJson());
        }
        response.setDatas(jsonArray);

        return response;
    }

    @Override
    public NotificationResponse sendNoti(String idSend, String idreceiver, String message, String action, int type) {
        NotificationResponse response = new NotificationResponse();
        Notification notification = new Notification();
        ObjectId objectId = new ObjectId();
        String id = objectId.toHexString();
        notification.set_id(id);
        notification.setIdSend(idSend);
        notification.setIdReceiver(idreceiver);
        notification.setMessage(message);
        notification.setAction(action);
        notification.setType(type);
        notification.setStatus(Notification.ACTICE);
        notification.setCreate_at(Calendar.getInstance().getTimeInMillis());
        notification.setUpdate_at(Calendar.getInstance().getTimeInMillis());
        MongoPool.log(Notification.class.getSimpleName(), notification.toDocument());

        DB db =  MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Users.class.getSimpleName());
        StringBuilder sb = new StringBuilder();
        sb.append("{$and: [{_id: #}]}");
        MongoCursor<Users> cursor = collection.find(sb.toString(), idSend).limit(1).as(Users.class);
        if (cursor.hasNext()){
            Users users = cursor.next();
            notification.setNameSend(users.getName());
            if(users.getAvatar() != null){
                notification.setAvataSend(users.getAvatar());
            }else {
                notification.setAvataSend("");
            }
            response.setData(notification);
        } else {
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Id khong ton tai");
        }
        Chat.sendNotification(response);
        return response;
    }

    @Override
    public NotificationResponse sendNotification(String idSend, String idReceiver, String message, String action, int type) {
        NotificationResponse response = new NotificationResponse();
        Notification notification = new Notification();
        ObjectId objectId = new ObjectId();
        String id = objectId.toHexString();
        notification.set_id(id);
        notification.setIdSend(idSend);
        notification.setIdReceiver(idReceiver);
        notification.setMessage(message);
        notification.setAction(action);
        notification.setType(type);
        notification.setStatus(Notification.ACTICE);
        notification.setCreate_at(Calendar.getInstance().getTimeInMillis());
        notification.setUpdate_at(Calendar.getInstance().getTimeInMillis());
        MongoPool.log(Notification.class.getSimpleName(), notification.toDocument());

        DB db =  MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Users.class.getSimpleName());
        StringBuilder sb = new StringBuilder();
        sb.append("{$and: [{_id: #}]}");
        MongoCursor<Users> cursor = collection.find(sb.toString(), idSend).limit(1).as(Users.class);
        if (cursor.hasNext()){
            Users users = cursor.next();
            notification.setNameSend(users.getName());
            if(users.getAvatar() != null){
                notification.setAvataSend(users.getAvatar());
            }else {
                notification.setAvataSend("");
            }
            response.setData(notification);
        } else {
            response.setError(ErrorCode.USER_NOT_EXIST);
            response.setMsg("Id khong ton tai");
        }
        Chat.sendNotification(response);
        return response;
    }

    @Override
    public NotificationResponse sendNotificationLogin(String idreceiver) {
        NotificationResponse response = new NotificationResponse();
        if (!ObjectId.isValid(idreceiver)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<Notification> cursor = null;
        MongoCollection collection = jongo.getCollection(Notification.class.getSimpleName());
        builder.append("{$and: [{idReceiver: #},{status: #}]}");
        cursor = collection.find(builder.toString(),idreceiver,Notification.ACTICE).as(Notification.class);
        JsonArray jsonArray = new JsonArray();
        while(cursor.hasNext()){
            NotificationResponse response2 = new NotificationResponse();
            Notification notification = cursor.next();
            MongoCollection collectionUser = jongo.getCollection(Users.class.getSimpleName());
            StringBuilder sb = new StringBuilder();
            sb.append("{$and: [{_id: #}]}");
            MongoCursor<Users> cursorUser = collectionUser.find(sb.toString(), notification.getIdSend()).limit(1).as(Users.class);
            if (cursorUser.hasNext()){
                Users users = cursorUser.next();
                notification.setNameSend(users.getName());
                if(users.getAvatar() != null){
                    notification.setAvataSend(users.getAvatar());
                }else {
                    notification.setAvataSend("");
                }
            }
            Chat.sendNotification(response2);
            jsonArray.add(notification.toJson());
        }
        response.setDatas(jsonArray);
        return response;
    }

    @Override
    public NotificationResponse updateStatus(String id, int status) {
        NotificationResponse response = new NotificationResponse();
        if (!ObjectId.isValid(id)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);

        MongoCursor<Notification> cursor = null;
        MongoCollection collection = jongo.getCollection(Notification.class.getSimpleName());
        StringBuilder builder = new StringBuilder();
        builder.append("{_id:#}");
        cursor = collection.find(builder.toString(),new ObjectId(id)).limit(1).as(Notification.class);
        if(cursor.hasNext()){
            Notification notification = cursor.next();
            notification.setStatus(status);
            collection.update("{_id:#}", new ObjectId(id)).with("{$set:{status:#}}",status);
            MongoCollection collectionUser = jongo.getCollection(Users.class.getSimpleName());
            StringBuilder sb = new StringBuilder();
            sb.append("{$and: [{_id: #}]}");
            MongoCursor<Users> cursorUser = collectionUser.find(sb.toString(), notification.getIdSend()).limit(1).as(Users.class);
            if (cursorUser.hasNext()){
                Users users = cursorUser.next();
                notification.setNameSend(users.getName());
                if(users.getAvatar() != null){
                    notification.setAvataSend(users.getAvatar());
                }else {
                    notification.setAvataSend("");
                }
                response.setData(notification);
            }else {
                response.setError(ErrorCode.ID_NOT_EXIST);
                response.setMsg("Id user send không tồn tại.");
            }
        }else {
            response.setError(ErrorCode.ID_NOT_EXIST);
            response.setMsg("Id không tồn tại.");
        }
        return response;
    }
}
