import vietung.it.dev.core.config.MongoPool;

import java.io.IOException;

public class MongoTest {
    public static void main(String[] args) {
        try {
            MongoPool.init();

            System.out.println("finnish!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
