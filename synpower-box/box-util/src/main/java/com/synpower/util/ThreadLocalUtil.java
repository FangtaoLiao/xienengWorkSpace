package com.synpower.util;

import java.util.Map;

public class ThreadLocalUtil {
    private static final ThreadLocal LOCAL = new ThreadLocal<>();

    public static Map<String, Object> getMap() {
        return (Map<String, Object>) LOCAL.get();
    }

    public static void setMap(Map<String, Object> map) {
        LOCAL.set(map);
    }
}
