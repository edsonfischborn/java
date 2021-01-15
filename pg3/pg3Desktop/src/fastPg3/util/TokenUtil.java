package fastPg3.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import sun.misc.BASE64Decoder;

/**
 *
 * @author Édson Fischborn
 */
public class TokenUtil {
    public static JsonObject decode(String fullToken){
        try {
            String token = fullToken.split(" ")[1];
            String[] splitString = token.split("\\.");
            String base64EncodedBody = splitString[1];

            String body = new String(new BASE64Decoder().decodeBuffer(base64EncodedBody));
            return JsonParser.parseString(body).getAsJsonObject();
        }catch (Exception ex){
           return null;
        }
    }
}
