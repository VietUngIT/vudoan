package vietung.it.dev.core.config;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import vietung.it.dev.core.models.Messages;

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
                // broadcast messages to all clients
                MongoPool.log(Messages.class.getSimpleName(),data.toDocument());
                server.getBroadcastOperations().sendEvent("chatevent", data);
            }
        });

        server.start();

        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }
}
