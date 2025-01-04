package com.e_commerce_creator.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class SystemUtils {
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


    public static void copyPropsFromSrcToDest(Object src, Object dest) {
        Objects.requireNonNull(src, "src should not be null");
        Objects.requireNonNull(dest, "dest should not be null");
        ModelMapper mapper = new ModelMapper();
        //mapped source should not be null
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        mapper.map(src, dest);//will return the new destination value
    }

    //todo: additionally and may be remove
    public static void replacePropsFromSrcToDest(Object src, Object dest, String fields) {
        Objects.requireNonNull(src, "src should not be null");
        Objects.requireNonNull(dest, "dest should not be null");
        Objects.requireNonNull(fields, "fields should not be null");
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(Configuration.AccessLevel.PRIVATE).setMatchingStrategy(MatchingStrategies.STRICT);
        if ("**".equals(fields)) {
            mapper.map(src, dest);
        } else {
            Set<String> fieldSet = Arrays.stream(fields.split(",")).map(String::trim).collect(Collectors.toSet());
            fieldSet.forEach(field -> {
                try {
                    Field srcField = src.getClass().getDeclaredField(field);
                    Field destField = src.getClass().getDeclaredField(field);
                    if (srcField != null && destField != null) {
                        srcField.setAccessible(true);
                        destField.setAccessible(true);
                        Object srcValue = srcField.get(src);
                        if (srcValue != null) destField.set(dest, srcValue);
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    log.error("Field not found :: {} ", field);
                }
            });
        }
    }

}
