package com.diy.framework.view;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private final Map<String, Object> model = new HashMap<String, Object>();

    public void addAttribute(String key, Object value) {
        model.put(key, value);
    }


}
