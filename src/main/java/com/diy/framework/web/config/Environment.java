package com.diy.framework.web.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Environment {
    private final Map<String, Object> env = new HashMap<>();
    public Environment() {
        Yaml yaml = new Yaml();
        try (InputStream in = Files.newInputStream(Paths.get("src/main/resources/application.yml"))) {
            // YML 데이터를 Map으로 로드
            Map<String, Object> obj = yaml.load(in);
            Set<String> keys = obj.keySet();
            Queue<String> queue = new LinkedList<>(keys);

            while (!queue.isEmpty()) {
                String key = queue.poll();
                Object value = obj.get(key);
                if (value instanceof Map) {
                    Map<String, Object> map = (Map<String, Object>) value;
                    obj.putAll(map);
                    queue.addAll(map.keySet());
                    env.put(key, map.keySet());
                    continue;
                }
                env.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Object getEnv(String key) {
        return env.get(key);
    }

    public String[] getPackages() {
        List<String> packages = new LinkedList<>();

        Queue<Object> queue = new LinkedList<>();
        Object packageNames = env.get("package");
        if (packageNames instanceof Set<?>) {
            queue.addAll((Set<?>) packageNames);
        }


        while (!queue.isEmpty()) {
            Object value = queue.poll();
            Object o = env.get(value.toString());
            if (o == null) {
                packages.add(value.toString());
                continue;
            }
            if (o instanceof Set<?> set) {
                queue.addAll(set);
                continue;
            }
            if (o instanceof String) {
                packages.add(o.toString());
            }
        }

        return packages.toArray(String[]::new);
    }
}
