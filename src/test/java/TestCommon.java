import com.google.gson.JsonArray;
import vietung.it.dev.core.utils.Utils;

public class TestCommon {
    public static void main(String[] args) {
        String abc = "[\"abc\",\"xyz\"]";
        JsonArray array = Utils.toJsonArray(abc);
        System.out.println(abc);
        System.out.println(array);
        System.out.println(array.toString());
    }
}
