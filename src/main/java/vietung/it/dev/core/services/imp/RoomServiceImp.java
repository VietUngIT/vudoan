package vietung.it.dev.core.services.imp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.RoomsResponse;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.*;
import vietung.it.dev.core.services.RoomService;
import vietung.it.dev.core.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RoomServiceImp implements RoomService {

    @Override
    public RoomsResponse createRoom(String name, String user, int type) {
        RoomsResponse response = new RoomsResponse();
        int typeRoom = Room.TYPE_ROOM_TWO;
        if (typeRoom != Room.TYPE_ROOM_TWO && typeRoom != Room.TYPE_ROOM_GROUP) {
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("type room không tồn tại");
            return response;
        }
        try {
            DB db = MongoPool.getDBJongo();
            Jongo jongo = new Jongo(db);

            JsonArray array = Utils.toJsonArray(user);
            List<String> lstUser = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                if (!ObjectId.isValid(array.get(i).getAsString())) {
                    response.setError(ErrorCode.NOT_A_OBJECT_ID);
                    response.setMsg("Id User không đúng.");
                    return response;
                }
                MongoCollection collectionIdUser = jongo.getCollection(Users.class.getSimpleName());
                MongoCursor<Users> cursorUsers = collectionIdUser.find("{_id:#}", array.get(i).getAsString()).as(Users.class);
                if (cursorUsers.hasNext()) {
                    lstUser.add(array.get(i).getAsString());
                } else {
                    response.setError(ErrorCode.INVALID_PARAMS);
                    response.setMsg("Id User không tôn tại.");
                    return response;
                }

            }
            if(typeRoom == Room.TYPE_ROOM_TWO){
                MongoCollection collectionRoom = jongo.getCollection(Room.class.getSimpleName());
                MongoCursor<Room> cursorRoomUser = collectionRoom.find("{user:{$all:#}}",lstUser).as(Room.class);
                if (cursorRoomUser.hasNext()) {
                    response.setError(ErrorCode.INVALID_PARAMS);
                    response.setMsg("Room đã tôn tại.");
                    return response;
                }
            }

            Room room = new Room();
            ObjectId _id = new ObjectId();
            room.set_id(_id.toHexString());
            room.setName(name);
            room.setUser(lstUser);
            room.setType(typeRoom);
            room.setStatus(Room.STATUS_ROOM_ACTIVATED);
            room.setCreate_at(Calendar.getInstance().getTimeInMillis());
            room.setUpdate_at(Calendar.getInstance().getTimeInMillis());
            MongoPool.log(Room.class.getSimpleName(), room.toDocument());
            response.setData(room.toJson());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public RoomsResponse editRoom(String idroom, String name, String user, int type) {
        RoomsResponse response = new RoomsResponse();
        return response;
    }

    @Override
    public RoomsResponse deleteRoom(String idroom) {
        RoomsResponse response = new RoomsResponse();
        if (!ObjectId.isValid(idroom)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Room.class.getSimpleName());
        collection.remove(new ObjectId(idroom));
        return response;
    }

    @Override
    public RoomsResponse getAllRoomByIdUserWithRoom(int page, int ofset, String iduser) {
        RoomsResponse response = new RoomsResponse();
        if (!ObjectId.isValid(iduser)) {
            response.setError(ErrorCode.NOT_A_OBJECT_ID);
            response.setMsg("Id không đúng.");
            return response;
        }
        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        StringBuilder builder = new StringBuilder();
        MongoCursor<Room> cursor = null;
        MongoCollection collection = jongo.getCollection(Room.class.getSimpleName());
        builder.append("{$and: [{user: #}]}");
        cursor = collection.find(builder.toString(),iduser).sort("{create_at:-1}").skip(page).limit(ofset).as(Room.class);
        JsonArray jsonArray = new JsonArray();
        while (cursor.hasNext()) {
            Room room = cursor.next();
            List<String> user = room.getUser();
            JsonArray users = new JsonArray();
            for(int i =0 ; i < user.size() ; i ++){
                MongoCollection collectionIdUser = jongo.getCollection(Users.class.getSimpleName());
                MongoCursor<Users> cursorUsers = collectionIdUser.find("{_id:#}", user.get(i)).as(Users.class);
                while (cursorUsers.hasNext()) {
                    users.add( cursorUsers.next().toJson());
                }
            }
            room.setUsers(users);
            MongoCollection collectionMessages = jongo.getCollection(Messages.class.getSimpleName());
            MongoCursor<Messages> cursorMessages = collectionMessages.find("{$and: [{idRoom: #}]}",room.get_id()).sort("{create_at:-1}").skip(0).limit(1).as(Messages.class);
            while (cursorMessages.hasNext()) {
                room.setMessages(cursorMessages.next().getMessage());
                room.setTime( cursorMessages.next().getCreate_at());
            }
            jsonArray.add(room.toJson());
        }
        response.setDatas(jsonArray);

        return response;
    }
}
