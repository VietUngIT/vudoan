import vietung.it.dev.core.utils.Utils;

public class JavaTest {
    public static void main(String[] args) {
        String abc = "[]";
        System.out.println(Utils.toJsonArray(abc));
        System.out.println(Utils.toJsonArray(abc).size());
    }
}
