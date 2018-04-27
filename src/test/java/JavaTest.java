import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.collections4.CollectionUtils;
import vietung.it.dev.core.models.Category;
import vietung.it.dev.core.utils.Utils;

public class JavaTest {
    public static void main(String[] args) {
        String abc = "[]";

        List<Category> a = new ArrayList<>();
        JsonArray b = new JsonArray();
        for(int i=0;i<5;i++){
            Category category = new Category();
            category.set_id(String.valueOf(i+1));
            category.setName("name: "+i);
            a.add(category);
        }
        for(int i=0;i<5;i++){
            JsonObject category = new JsonObject();
            category.addProperty("id",i);
            category.addProperty("name","name "+i);
            b.add(category);
        }

        b.add("Blue");
        b.add("pink");
        b.add("yellow");
        b.add("red");
//        System.out.println("n="+CollectionUtils.intersection(a,b).size());
        System.out.println("b: "+b);
        System.out.println(Utils.toJsonStringGson(a));
        //        System.out.println(Utils.toJsonArray(abc));
//        System.out.println(Utils.toJsonArray(abc).size());
    }
}
