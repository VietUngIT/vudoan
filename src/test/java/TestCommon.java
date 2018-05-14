import com.google.gson.JsonArray;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCursor;
import org.jongo.QueryModifier;
import vietung.it.dev.core.config.MongoPool;
import vietung.it.dev.core.consts.Variable;
import vietung.it.dev.core.models.Category;
import vietung.it.dev.core.models.CategoryMarketPrice;
import vietung.it.dev.core.models.Demo;
import vietung.it.dev.core.models.MarketPrice;
import vietung.it.dev.core.utils.Utils;

import java.io.IOException;
import java.util.Calendar;

public class TestCommon {
    public static void main(String[] args) {
        try {
            MongoPool.init();
//            MongoDatabase database = MongoPool.getDB();
//            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions();
//            createCollectionOptions.capped(true);
//            createCollectionOptions.sizeInBytes(10000);
//            database.createCollection(Variable.MG_COMMENTS_NEWS,createCollectionOptions);

            //Insert comment to mongo
//            DB db = MongoPool.getDBJongo();
//            Jongo jongo = new Jongo(db);
//            org.jongo.MongoCollection collectionIdCate = jongo.getCollection("Demo");
//            QueryModifier queryModifier = new QueryModifier() {
//                @Override
//                public void modify(DBCursor dbCursor) {
//                    dbCursor.addOption(Bytes.QUERYOPTION_TAILABLE | Bytes.QUERYOPTION_AWAITDATA);
//                }
//            };
//            MongoCursor<Demo> cursor = collectionIdCate.find().with(queryModifier).as(Demo.class);
//            System.out.println("== open cursor ==");
//
//            Runnable task = () -> {
//                System.out.println("\tWaiting for events");
//                while (cursor.hasNext()) {
//                    Demo obj = cursor.next();
//                    System.out.println( obj.toJson() );
//
//                }
//            };
//            new Thread(task).start();
            Calendar calendar = Calendar.getInstance();
            System.out.println(calendar.get(Calendar.YEAR));
            System.out.println(calendar.get(Calendar.MONTH));
            System.out.println(calendar.get(Calendar.DATE));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
