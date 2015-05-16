package com.chenyueling.subscribe.utils;

import java.util.HashMap;
import java.util.Map;


public class CollectionUtil {
    public static MapBuilder map() {
        return new MapBuilder();
    }

    public static class MapBuilder {
        private Map<String, String> data;

        private MapBuilder() {
            data = new HashMap<String, String>();
        }

        public MapBuilder put(String key, String value) {
            if (value != null) {
                data.put(key, value);
            }
            return this;
        }

        public Map<String, String> build() {
            return data;
        }
    }

    private CollectionUtil() {}
}
