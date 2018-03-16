package vietung.it.dev.core.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CloudinaryConfig {
    private static final String CLOUDINARY_CONFIG_FILE = "config/resource/cloudinary.properties";
    public static String CLOUDINARY_NAME = "";
    public static String CLOUDINARY_API_KEY = "";
    public static String CLOUDINARY_API_SECRET = "";
    private static Cloudinary cloudinary;
    public static void loadConfig() throws IOException {
        Properties prop = new Properties();
        InputStream input = new FileInputStream(CLOUDINARY_CONFIG_FILE);
        prop.load(input);
        CLOUDINARY_NAME = prop.getProperty("cloud_name");
        CLOUDINARY_API_KEY = prop.getProperty("api_key");
        CLOUDINARY_API_SECRET = prop.getProperty("api_secret");
    }
    public static Cloudinary getInstance(){
        if(cloudinary==null){
            cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", CLOUDINARY_NAME, "api_key", CLOUDINARY_API_KEY,"api_secret", CLOUDINARY_API_SECRET));
        }
        return cloudinary;
    }
}
