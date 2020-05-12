package wb.im.utils.encrypt;

import java.util.Base64;

/**
 *
 */
public class Base64Util {


    public static String encode(byte[] bytes) throws Exception {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] decode(String str) throws Exception {
        return Base64.getDecoder().decode(str);
    }
}
