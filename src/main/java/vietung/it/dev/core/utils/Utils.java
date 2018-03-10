package vietung.it.dev.core.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.core.config.MongoConfig;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.models.Users;

import java.security.MessageDigest;
import java.util.Random;

public class Utils {
    public static final Random rd = new Random();
    private static Gson gson = new Gson();
    public static JsonObject toJsonObject(String json) {
        try {
            return gson.fromJson(json, JsonObject.class);
        } catch (Exception e) {
            return null;
        }
    }
    public static String toJsonStringGson(Object obj) {
        return gson.toJson(obj);
    }
    public static Users getUserByPhone(String phone) {
        DB db =  MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Users.class.getSimpleName());
        StringBuilder sb = new StringBuilder();
        sb.append("{$and: [{phone: #}]}");
        MongoCursor<Users> cursor = collection.find(sb.toString(), phone).limit(1).as(Users.class);
        if (cursor.hasNext()){
            return cursor.next();
        }
        return null;
    }
    public static String sha256(String src) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(src.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
