package com.diy.framework.web.server.mv;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Model {
    private final Map<String, Object> attributes = new HashMap<>();

    public Model() {}

    public Model(final Map<String, Object> attributes) {
        this.attributes.putAll(attributes);
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public boolean containsAttribute(String attributeName) {
        return this.attributes.containsKey(attributeName);
    }

    public Object getAttribute(String attributeName) {
        return this.attributes.get(attributeName);
    }

    public void setAttributes(final Map<String, Object> attributes) {
        this.attributes.putAll(attributes);
    }

    public void setAttribute(final String attributeName, final Object value) {
        attributes.put(attributeName, value);
    }
}
