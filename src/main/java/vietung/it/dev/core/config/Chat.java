package vietung.it.dev.core.config;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.mongodb.DB;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vietung.it.dev.apis.launcher.APILauncher;
import vietung.it.dev.apis.response.MessagesResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.Messages;
import vietung.it.dev.core.models.Room;
import vietung.it.dev.core.services.UploadService;
import vietung.it.dev.core.services.imp.UploadServiceImp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Chat {
    public static void init() throws IOException, InterruptedException {
        SoketioConfig.init();

        Configuration config = new Configuration();
        config.setHostname(SoketioConfig.SOKETIO_HOST);
        config.setPort(SoketioConfig.SOKETIO_PORT);
        config.setMaxFramePayloadLength(10485760);

        final SocketIOServer server = new SocketIOServer(config);

        DB db = MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collectionRoom = jongo.getCollection(Room.class.getSimpleName());
        MongoCursor<Room> cursorRoom = collectionRoom.find().sort("{create_at:-1}").as(Room.class);
        while(cursorRoom.hasNext()){
            Room room = cursorRoom.next();
            final SocketIONamespace socketIONamespace = server.addNamespace("/"+room.get_id());
            socketIONamespace.addEventListener("chatevent", Messages.class, new DataListener<Messages>() {
                @Override
                public void onData(SocketIOClient client, Messages data, AckRequest ackRequest) {
                    MessagesResponse response = new MessagesResponse();
                    // broadcast messages to all clients
                    if(data.getType() == Messages.TYPE_MESSAGE_IMAGE){
                        UploadService service = new UploadServiceImp();
                        try {
                            String urlImage = service.uploadImage(data.getMessage());
                            System.out.println(urlImage);
                            data.setMessage(urlImage);
                            MongoPool.log(Messages.class.getSimpleName(),data.toDocument());
                            response.setData(data);
                        } catch (IOException e) {
                            response.setError(ErrorCode.UPLOAD_IMAGE_ERROR);
                        }
                    } else {
                        MongoPool.log(Messages.class.getSimpleName(),data.toDocument());
                        response.setData(data);
                    }
                    socketIONamespace.getBroadcastOperations().sendEvent("chatevent", response);
                }
            });
        }


        server.addEventListener("chatevent", Messages.class, new DataListener<Messages>() {
            @Override
            public void onData(SocketIOClient client, Messages data, AckRequest ackRequest) {
                MessagesResponse response = new MessagesResponse();
                // broadcast messages to all clients
                if(data.getType() == Messages.TYPE_MESSAGE_IMAGE){
                    UploadService service = new UploadServiceImp();
                    try {
                        String urlImage = service.uploadImage(data.getMessage());
                        System.out.println(urlImage);
                        data.setMessage(urlImage);
                        MongoPool.log(Messages.class.getSimpleName(),data.toDocument());
                        response.setData(data);
                    } catch (IOException e) {
                        response.setError(ErrorCode.UPLOAD_IMAGE_ERROR);
                    }
                } else {
                    MongoPool.log(Messages.class.getSimpleName(),data.toDocument());
                    response.setData(data);
                }
                server.getBroadcastOperations().sendEvent("chatevent", response);
            }
        });

        server.start();

        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }
}
