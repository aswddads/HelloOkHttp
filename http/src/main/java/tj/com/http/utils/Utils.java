package tj.com.http.utils;

/**
 * Created by Jun on 17/8/6.
 */

public class Utils {

    public static boolean isExist(String className,ClassLoader loader){
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
