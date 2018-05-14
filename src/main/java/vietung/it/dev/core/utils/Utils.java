package vietung.it.dev.core.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vietung.it.dev.apis.response.BaseResponse;
import vietung.it.dev.apis.response.SimpleResponse;
import vietung.it.dev.core.config.MongoConfig;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.ErrorCode;
import vietung.it.dev.core.models.Users;

import java.security.MessageDigest;
import java.util.Calendar;
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
    public static JsonArray toJsonArray(String json) {
        try {
            return gson.fromJson(json, JsonArray.class);
        } catch (Exception e) {
            return null;
        }
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
    public static Users getUserById(String id) {
        if (!ObjectId.isValid(id)) {
            return null;
        }
        DB db =  MongoPool.getDBJongo();
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(Users.class.getSimpleName());
        StringBuilder sb = new StringBuilder();
        sb.append("{$and: [{_id: #}]}");
        MongoCursor<Users> cursor = collection.find(sb.toString(), id).limit(1).as(Users.class);
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

    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit.equals("K")) {
            dist = dist * 1.609344;
        }
        return (dist);
    }
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static BaseResponse notifiError(int errorCode, String msg){
        SimpleResponse response = new SimpleResponse();
        response.setError(errorCode);
        response.setMsg(msg);
        return response;
    }
    public static long getStartDay(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }
    public static long getEndDay(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,990);
        return calendar.getTimeInMillis();
    }
}
