package com.diy.app;

import java.util.function.Function;

public sealed interface Result<T> permits Result.Success, Result.Failure {
    record Success<T>(T value) implements Result<T> {
        @Override
        public <R> Result<R> map(Function<T, R> function) {
            return new Success<>(function.apply(value));
        }
    }
    record Failure<T>(Exception error ) implements Result<T> {
        @Override
        public <R> Result<R> map(Function<T, R> function) {
            return (Result<R>) this;
        }
    }

    <R> Result<R> map(Function<T, R> function);
}
