package vietung.it.dev.apis.launcher;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.apache.kafka.clients.producer.Producer;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vietung.it.dev.core.config.*;
import vietung.it.dev.core.services.UserService;
import vietung.it.dev.core.services.imp.UserServiceImp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class APILauncher {
    private static Logger logger = LoggerFactory.getLogger(APILauncher.class.getName());


    public static void main(String[] args) {
        runVertx();
    }

    public static void runVertx() {
        try {
            PropertyConfigurator.configure("config/resource/log4j.properties");
            logger.info("Logger is initialed");
            APIConfig.init();
            logger.info("APIConfig is initialed");
            MongoPool.init();
            logger.info("MongoPool is initialed");
            CloudinaryConfig.loadConfig();
            logger.info("Cloudinary is initialed");
            Vertx vertx = Vertx.vertx();
            int procs = Runtime.getRuntime().availableProcessors();
            vertx.deployVerticle(VertxHttpConfigServer.class.getName(),
                    new DeploymentOptions().setInstances(procs*2), event -> {
                        if (event.succeeded()) {
                            logger.debug("Vert.x application is started!");
                        } else {

                            logger.error("Unable to start your application", event.cause());
                        }
                    });
            ExecutorService service = Executors.newFixedThreadPool(1);
            service.execute(new Runnable() {
                @Override
                public void run() {
                    KafkaProduce.init();
                }
            });
            ExecutorService service2 = Executors.newFixedThreadPool(1);
            service.execute(new Runnable() {
                @Override
                public void run() {
                    KafkaConsume.init();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
