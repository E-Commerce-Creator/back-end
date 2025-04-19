package com.e_commerce_creator.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;
import java.util.*;
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

    public static Pageable createPageable(Integer page, Integer size, String sortBy, Boolean sortDesc) {
        Sort sort = Sort.by((sortDesc) ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        return PageRequest.of(page, size, sort);
    }

    public static Pageable createPageable(Integer page, Integer size) {
        return PageRequest.of(page, size);
    }

    public static Pageable createPageable(Integer page, Integer size, List<String> sortBy, List<Boolean> sortDesc) {
        List<Sort.Order> orders = new ArrayList<>();
        for (int i = 0; i < sortBy.size(); i++) {
            if (i > sortDesc.size() - 1) {
                orders.add(Sort.Order.asc(sortBy.get(i)));
            } else if (sortDesc.get(i)) {
                orders.add(Sort.Order.desc(sortBy.get(i)));
            } else {
                orders.add(Sort.Order.asc(sortBy.get(i)));
            }
        }
        Sort sort = Sort.by(orders);
        return PageRequest.of(page, size, sort);
    }

    public static Pageable createPageable(Integer page, Integer size, String sortBy, Sort.Direction direction) {
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }

    public static Pageable createSortOnlyPageable(String sortBy, Sort.Direction direction) {
        return PageRequest.of(0, Integer.MAX_VALUE, Sort.by(direction, sortBy));
    }

    public static JsonNode getCurrentUserDetailsInJson() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        node.set("principal", mapper.valueToTree(authentication.getPrincipal()));
        node.set("authorities", mapper.valueToTree(authentication.getAuthorities()));
//        node.set("authorities", mapper.valueToTree(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())));
        node.put("credentials", String.valueOf(authentication.getCredentials()));
        return node;
    }

    public static TypedQuery setQueryParameters(TypedQuery query, Map<String, Object> parameters) {
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof List) {
                query.setParameter(key, (List<?>) value);
            } else {
                query.setParameter(key, value);
            }
        }
        return query;
    }

}
