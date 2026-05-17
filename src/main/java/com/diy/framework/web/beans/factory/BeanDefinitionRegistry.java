package com.diy.framework.web.beans.factory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanDefinitionRegistry {

    private final Map<String, BeanDefinition> definitionsByName = new HashMap<>();

    public void register(final BeanDefinition beanDefinition) {
        if (definitionsByName.containsKey(beanDefinition.getBeanName())) {
            throw new IllegalStateException("같은 이름의 빈이 이미 등록되어 있습니다: " + beanDefinition.getBeanName());
        }

        definitionsByName.put(beanDefinition.getBeanName(), beanDefinition);
    }

    public BeanDefinition getByName(final String beanName) {
        BeanDefinition beanDefinition = definitionsByName.get(beanName);
        if (beanDefinition == null) {
            throw new IllegalStateException("등록되지 않은 빈입니다: " + beanName);
        }
        return beanDefinition;
    }

    public List<BeanDefinition> getByType(final Class<?> type) {
        return definitionsByName.values().stream()
            .filter(definition -> type.isAssignableFrom(definition.getBeanClass()))
            .toList();
    }

    public Collection<BeanDefinition> getAll() {
        return definitionsByName.values();
    }
}
