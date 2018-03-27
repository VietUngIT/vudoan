package vietung.it.dev.core.config;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import vietung.it.dev.apis.response.MessagesResponse;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.Messages;
import vietung.it.dev.core.services.UploadService;
import vietung.it.dev.core.services.imp.UploadServiceImp;

import java.io.IOException;

public class Chat {
    public static void init() throws IOException, InterruptedException {
        SoketioConfig.init();

        Configuration config = new Configuration();
        config.setHostname(SoketioConfig.SOKETIO_HOST);
        config.setPort(SoketioConfig.SOKETIO_PORT);
        config.setMaxFramePayloadLength(10485760);

        final SocketIOServer server = new SocketIOServer(config);
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
                    } catch (IOException e) {
                        response.setError(ErrorCode.UPLOAD_IMAGE_ERROR);
                    }
                }
                response.setData(data.toJson());
                server.getBroadcastOperations().sendEvent("chatevent", response);
            }
        });

        server.start();

        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }
}
