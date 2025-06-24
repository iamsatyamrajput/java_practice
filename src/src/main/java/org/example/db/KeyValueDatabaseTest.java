package org.example.db;

public class KeyValueDatabaseTest {
    public static void main(String[] args) {
        KeyValueDatabase<String, String> db = new KeyValueDatabase<>();
        db.init();
        
        db.set("Test", "Test1");
        System.out.println(db.get("Test"));
        System.out.println(db.delete("Test"));
    }
}
