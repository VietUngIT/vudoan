package vietung.it.dev.core.config;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.google.gson.JsonArray;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vietung.it.dev.apis.launcher.APILauncher;
import vietung.it.dev.apis.response.MessagesResponse;
import vietung.it.dev.apis.response.NotificationResponse;
import vietung.it.dev.apis.response.RoomsResponse;
import vietung.it.dev.apis.response.UserResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.*;
import vietung.it.dev.core.services.NotificationService;
import vietung.it.dev.core.services.UploadService;
import vietung.it.dev.core.services.imp.NotificationServiceImp;
import vietung.it.dev.core.services.imp.UploadServiceImp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Chat {
    private static SocketIOServer server;

    public static void init() throws IOException, InterruptedException {
        SoketioConfig.init();

        Configuration config = new Configuration();
        config.setHostname(SoketioConfig.SOKETIO_HOST);
        config.setPort(SoketioConfig.SOKETIO_PORT);
        config.setMaxFramePayloadLength(10485760);

        server = new SocketIOServer(config);

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collectionRoom = jongo.getCollection(Room.class.getSimpleName());
        MongoCursor<Room> cursorRoom = collectionRoom.find().sort("{create_at:-1}").as(Room.class);
        //chatevent room
        while (cursorRoom.hasNext()) {
            Room room = cursorRoom.next();
            addNamespace(room);
        }
        //chatevent full server
        server.addEventListener("chatevent", Messages.class, new DataListener<Messages>() {
            @Override
            public void onData(SocketIOClient client, Messages data, AckRequest ackRequest) {
                MessagesResponse response = new MessagesResponse();
                // broadcast messages to all clients
                if (data.getType() == Messages.TYPE_MESSAGE_IMAGE) {
                    UploadService service = new UploadServiceImp();
                    try {
                        String urlImage = service.uploadImage(data.getMessage());
                        System.out.println(urlImage);
                        data.setMessage(urlImage);
                        MongoPool.log(Messages.class.getSimpleName(), data.toDocument());
                        response.setData(data);
                    } catch (IOException e) {
                        response.setError(ErrorCode.UPLOAD_IMAGE_ERROR);
                    }
                } else {
                    MongoPool.log(Messages.class.getSimpleName(), data.toDocument());
                    response.setData(data);
                }
                server.getBroadcastOperations().sendEvent("chatevent", response);
            }
        });
        //online
        server.addEventListener("online", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
                UserResponse response = new UserResponse();
                if (ObjectId.isValid(data.toString())) {
                    MongoCollection collectionUsers = jongo.getCollection(Users.class.getSimpleName());
                    StringBuilder sb = new StringBuilder();
                    sb.append("{_id: #}");
                    MongoCursor<Users> cursorUsers = collectionRoom.find(sb.toString(), new ObjectId(data)).limit(1).as(Users.class);
                    if(cursorUsers.hasNext()){
                        Users users = cursorUsers.next();
                        collectionUsers.update("{_id:#}",new ObjectId(data)).with("{$set:{status:#}}",Users.STATUS_ONLINE);
                        users.setStatus(Users.STATUS_ONLINE);
                        response.setUsers(users);

                        // broadcast messages to all clients
                        server.getBroadcastOperations().sendEvent("online", response);
                    }
                }
            }
        });
        //offline
        server.addEventListener("offline", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
                UserResponse response = new UserResponse();
                if (ObjectId.isValid(data.toString())) {
                    MongoCollection collectionUsers = jongo.getCollection(Users.class.getSimpleName());
                    StringBuilder sb = new StringBuilder();
                    sb.append("{_id: #}");
                    MongoCursor<Users> cursorUsers = collectionRoom.find(sb.toString(), new ObjectId(data)).limit(1).as(Users.class);
                    if(cursorUsers.hasNext()){
                        Users users = cursorUsers.next();
                        collectionUsers.update("{_id:#}",new ObjectId(data)).with("{$set:{status:#}}",Users.STATUS_OFFLINE);
                        users.setStatus(Users.STATUS_OFFLINE);
                        response.setUsers(users);

                        // broadcast messages to all clients
                        server.getBroadcastOperations().sendEvent("offline", response);
                    }
                }
            }
        });

        server.start();

        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }

    public static void addNamespace(Room room) {
        final SocketIONamespace socketIONamespace = server.addNamespace("/" + room.get_id());
        socketIONamespace.addEventListener("chatevent", Messages.class, new DataListener<Messages>() {
            @Override
            public void onData(SocketIOClient client, Messages data, AckRequest ackRequest) {
                MessagesResponse response = new MessagesResponse();
                // broadcast messages to all clients
                if (data.getType() == Messages.TYPE_MESSAGE_IMAGE) {
                    UploadService service = new UploadServiceImp();
                    try {
                        String urlImage = service.uploadImage(data.getMessage());
                        System.out.println(urlImage);
                        data.setMessage(urlImage);
                        MongoPool.log(Messages.class.getSimpleName(), data.toDocument());
                        response.setData(data);
                    } catch (IOException e) {
                        response.setError(ErrorCode.UPLOAD_IMAGE_ERROR);
                    }
                } else {
                    MongoPool.log(Messages.class.getSimpleName(), data.toDocument());
                    response.setData(data);
                }
                socketIONamespace.getBroadcastOperations().sendEvent("chatevent", response);
                NotificationService notificationService = new NotificationServiceImp();
                if (ObjectId.isValid(data.getIdRoom())) {
                    DB db = MongoPool.getDBJongo();
                    Jongo jongo = new Jongo(db);
                    MongoCollection collectionRoom = jongo.getCollection(Room.class.getSimpleName());
                    MongoCursor<Room> cursorRoomUser = collectionRoom.find("{_id:#}",new ObjectId(data.getIdRoom())).as(Room.class);
                    if (cursorRoomUser.hasNext()) {
                        Room room = cursorRoomUser.next();
                        List<String> listuser = room.getUser();
                        JsonArray users = new JsonArray();
                        for(int i =0 ; i < listuser.size() ; i ++){
                            if(!listuser.get(i).equals(data.getIdUser())) {
                                notificationService.sendNotification(data.getIdUser(),listuser.get(i),"Bạn có tin nhắn : ",data.getIdRoom(),2);
                            }
                        }
                    }
                }
            }
        });
    }

    public static void sendNotification(NotificationResponse response){
        final SocketIONamespace socketIONamespace = server.addNamespace("/" + response.getData().getIdReceiver());
        socketIONamespace.getBroadcastOperations().sendEvent("notification", response);
    }
}