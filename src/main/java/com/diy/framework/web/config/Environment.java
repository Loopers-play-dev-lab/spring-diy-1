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
            env.putAll(obj);

            Queue<String> queue = new LinkedList<>(obj.keySet());

            while (!queue.isEmpty()) {
                String key = queue.poll();
                Object value = obj.get(key);
                if (value instanceof Map) {
                    Map<String, Object> map = (Map<String, Object>) value;
                    obj.putAll(map);
                    queue.addAll(map.keySet());
                    env.put(key, map.keySet());
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

        Queue<String> queue = new LinkedList<>(Arrays.asList(getEnv("package").toString().split(",")));

        while (!queue.isEmpty()) {
            String value = queue.poll();
            if (env.containsKey(value)) {
                queue.addAll(Arrays.asList(getEnv(value).toString().split(",")));
                continue;
            }
            packages.add(value);
        }

        return packages.toArray(new String[0]);
    }
}
