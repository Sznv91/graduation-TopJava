package ru.topjava.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import ru.topjava.entity.Restaurant;

public class JsonUtil {

    public static String getJsonString(Restaurant restaurant) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JSR310Module());
//        String result = "";
        try {
            return mapper.writeValueAsString(restaurant);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Что - то пошло не так с JSON");
        }
    }
}
