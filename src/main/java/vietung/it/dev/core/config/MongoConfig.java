package vietung.it.dev.core.config;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class MongoConfig {
    private static final String MONGO_CONFIG_FILE = "config/resource/mongodb.properties";
    public static String MONGODB_HOST = "";
    public static String MONGODB_DATABASE = "";
    public static String MONGODB_USERNAME = "";
    public static String MONGODB_PASSWORD = "";
    public static int MONGODB_PORT = 0;

    public static void init() throws IOException {
        Properties prop = new Properties();
        InputStream input = new FileInputStream(MONGO_CONFIG_FILE);
        prop.load(input);
        MONGODB_HOST = prop.getProperty("host");
        MONGODB_DATABASE = prop.getProperty("database");
        MONGODB_PORT = Integer.parseInt(prop.getProperty("port"));
        MONGODB_USERNAME = prop.getProperty("username");
        MONGODB_PASSWORD = prop.getProperty("password");
    }

}
