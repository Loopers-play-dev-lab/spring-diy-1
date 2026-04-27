package com.diy.app.domain;

import org.jetbrains.annotations.NotNull;

public record Price(
        long value
) {
    public static Price of(long value) {
        return new Price(value);
    }

    @NotNull
    @Override
    public String toString() {
        return Long.valueOf(this.value).toString();
    }
}