package com.diy.framework.web.beans.factory;

public interface DependencyResolver {
    <T> T resolve(Class<T> type);
}
