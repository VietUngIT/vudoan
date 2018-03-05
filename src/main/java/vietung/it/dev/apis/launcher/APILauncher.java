package vietung.it.dev.apis.launcher;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vietung.it.dev.core.config.APIConfig;
import vietung.it.dev.core.config.MongoConfig;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.config.VertxHttpConfigServer;
import vietung.it.dev.core.services.UserService;
import vietung.it.dev.core.services.imp.UserServiceImp;

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
            //UserService service = new UserServiceImp();
            //service.register("VietUngIT","01649216037","123456",4);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
