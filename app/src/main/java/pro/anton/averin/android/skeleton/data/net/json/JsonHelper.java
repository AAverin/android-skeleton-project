package pro.anton.averin.android.skeleton.data.net.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Created by AAverin on 06.11.13.
 */
@SuppressWarnings("DefaultFileTemplate")
public class JsonHelper {

    private static ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        objectMapper.registerModule(module);
    }
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static <T> T parseJson(JsonNode node, Class<T> targetClass) {
        try {
            return JsonHelper.getObjectMapper().treeToValue(node, targetClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

}
