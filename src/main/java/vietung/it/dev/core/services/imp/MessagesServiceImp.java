package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.MessagesResponse;
import vietung.it.dev.apis.response.NewsResponse;
import vietung.it.dev.apis.response.UserResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.Messages;
import vietung.it.dev.core.models.News;
import vietung.it.dev.core.models.Users;
import vietung.it.dev.core.services.MessagesService;
import vietung.it.dev.core.services.UserService;
import vietung.it.dev.core.utils.Utils;

import java.util.Calendar;

public class MessagesServiceImp implements MessagesService {

    @Override
    public MessagesResponse getAllMessagesByRoomWithMessagesest(int page, int ofset, String idroom) {
        MessagesResponse response = new MessagesResponse();
        if (!ObjectId.isValid(idroom)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<Messages> cursor = null;
        MongoCollection collection = jongo.getCollection(News.class.getSimpleName());
        builder.append("{$and: [{idRoom: #}]}");
        cursor = collection.find(builder.toString(),idroom).sort("{create_at:-1}").skip(page*ofset).limit(ofset).as(Messages.class);
//        MongoCollection collectionCate = jongo.getCollection(Variable.MG_CATEGORY_NEWS);
//        MongoCursor<Category> cursorCate = collectionCate.find("{_id:#}", new ObjectId(idcate)).as(Category.class);
//        String nameType = "";
//        if(cursorCate.hasNext()){
//            nameType = cursorCate.next().getName();
//        }
        JsonArray jsonArray = new JsonArray();
        while(cursor.hasNext()){
            Messages messages = cursor.next();
//            news.setNameCateNews(nameType);
            jsonArray.add(messages.toJson());
        }
        response.setDatas(jsonArray);

        return response;
    }
}
