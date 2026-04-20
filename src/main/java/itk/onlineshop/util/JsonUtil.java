package itk.onlineshop.util;

import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class JsonUtil {

    private final ObjectMapper mapper = new ObjectMapper();

    public String toJson(Object obj) throws Exception {
        return mapper.writeValueAsString(obj);
    }

    public <T> T fromJson(String json, Class<T> clazz) throws Exception {
        return mapper.readValue(json, clazz);
    }
}