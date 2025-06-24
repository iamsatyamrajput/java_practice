package org.example.hashmapExample;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class HashMap1 {
    public static void main(String[] args) {
        HashMap<Integer, String> map = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            map.put(i, "value" + i);
        }
        map.put(100000000, "value1");
        
        ConcurrentHashMap<Integer, String> concurrentMap = new ConcurrentHashMap<>();
        concurrentMap.put(1, "value1");
    }
}
