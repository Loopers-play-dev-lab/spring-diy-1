package com.diy.module.database;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InMemoryDatabase {
    private static InMemoryDatabase instance;
    public static Map<String, Object> db = new HashMap();

    private InMemoryDatabase() {}
    public static InMemoryDatabase getInstance() {
        if (instance == null) {
            instance = new InMemoryDatabase();
        }
        return instance;
    }

    public void create(String key, Object value) {
        db.put(key, value);
    }
    public void update(String key, Object value) {
        db.put(key, value);
    }

    public Object get(String key) {
        return db.get(key);
    }

    public List<Object> getAll(String key) {
        List<Object> list = new LinkedList<>();
        db.forEach((k, v) -> {
            if (k.startsWith(key)) {
                list.add(v);
            }
        });
        return list;
    }

    public void delete(String key) {
        db.remove(key);
    }

    public void clear() {
        db.clear();
    }
}
