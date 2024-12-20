package com.e_commerce_creator.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SystemUtil {
    public static String writeObjectAsString(Object object) {
        String result = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            result = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        return result;
    }

    public static JsonNode convertStringToJsonNode(Object json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        if (json != null) {
            if (!(json instanceof String)) return mapper.readTree(mapper.writeValueAsString(json));
            else return mapper.readTree((String) json);
        }
        return null;
    }

}
