package org.example;

import java.util.HashMap;

public class KeyValueDatabase<K,V> {
    HashMap<K,V> database;
    public void init() {
         database = new HashMap<>();
    }
    public void set(K key,V value) {
        database.put(key, value);
    }
    public V get(K key) {
        V value = null;
        if(!database.containsKey(key)) {
            System.out.println("Key doesn't exist in db: "+ key);
        } else {
            value = database.get(key);
        } 
        return value;
    }
    
    public boolean delete(K key) {
        if(!database.containsKey(key)) {
            System.out.println("Key doesn't exist in db: "+ key);
            return false;
        } else {
            System.out.println("Key removed from db: "+ key);
            database.remove(key);
            return false;
        }
    }
}
