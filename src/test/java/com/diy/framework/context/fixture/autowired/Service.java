package com.diy.framework.context.fixture.autowired;

import com.diy.framework.context.annotation.Autowired;
import com.diy.framework.context.annotation.Component;

@Component
public class Service {
    private final Repository repository;

    public Service() {
        this.repository = null;
    }

    @Autowired
    public Service(Repository repository) {
        this.repository = repository;
    }

    public Repository getRepository() {
        return this.repository;
    }
}
