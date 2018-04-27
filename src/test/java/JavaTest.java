import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import vietung.it.dev.core.models.Category;

public class JavaTest {
    public static void main(String[] args) {
        String abc = "[]";

        List<Category> a = new ArrayList<>();
        List<String> b = new ArrayList<>();
//        a.add("black");
//        a.add("blue");
//        a.add("green");
//        a.add("red");
//        a.add("pink");

        b.add("Blue");
        b.add("pink");
        b.add("yellow");
        b.add("red");
//        System.out.println("n="+CollectionUtils.intersection(a,b).size());
        for (int i=0;i<5;i++){
            Category category = new Category();
            category.setName("name "+i);
            category.set_id(String.valueOf(i));
            a.add(category);
        }
        for (Category a1: a){
            System.out.println(a1.get_id()+" - "+a1.getName());
        }
        System.out.println("-----------");
        int i=0;
        for (Category c : a){
            c.setName("name "+i+" update.");
            i++;
        }
        for (Category a1: a){
            System.out.println(a1.get_id()+" - "+a1.getName());
        }
        //        System.out.println(Utils.toJsonArray(abc));
//        System.out.println(Utils.toJsonArray(abc).size());
    }
}
