package com.diy.framework.web.beans.factory.support;

import com.diy.framework.web.annotations.Component;

@Component("namedService")
public class NamedService {
    public final NamedRepository namedRepository;

    public NamedService(NamedRepository namedRepository) {
        this.namedRepository = namedRepository;
    }
}
