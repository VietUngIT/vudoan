package vietung.it.dev.core.config;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SoketioConfig {
    private static final String SOKETIO_CONFIG_FILE = "config/resource/soketio.properties";
    public static String SOKETIO_HOST = "";
    public static int SOKETIO_PORT = 0;

    public static void init() throws IOException {
        Properties prop = new Properties();
        InputStream input = new FileInputStream(SOKETIO_CONFIG_FILE);
        prop.load(input);
        SOKETIO_HOST = prop.getProperty("host");
        SOKETIO_PORT = Integer.parseInt(prop.getProperty("port"));
    }
}
