package com.bol.game.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {

    public static String toJson(Object object) {

        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            log.error("Object couldn't serialize as JSON.", e);
            log.error(object.toString());
        }

        return "";
    }
}
