package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
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
import vietung.it.dev.core.models.ForumQuestion;
import vietung.it.dev.core.models.Messages;
import vietung.it.dev.core.models.Notification;
import vietung.it.dev.core.services.NotificationService;

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
            jsonArray.add(notification.toJson());
        }
        response.setDatas(jsonArray);

        return response;
    }

    @Override
    public void sendNotification(String idSend, String idReceiver, String message, String action, int type) {
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
        MongoPool.log(Notification.class.getSimpleName(), notification.toDocument());
        response.setData(notification);
        Chat.sendNotification(response);
    }
}
