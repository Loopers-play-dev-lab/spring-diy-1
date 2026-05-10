package com.diy.framework.web.server;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private final Map<String, Object> attributes = new HashMap<>();

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public boolean containsAttribute(String attributeName) {
        return this.attributes.containsKey(attributeName);
    }

    public Object getAttribute(String attributeName) {
        return this.attributes.get(attributeName);
    }

    public void setAttribute(final String attributeName, final Object value) {
        attributes.put(attributeName, value);
    }
}
