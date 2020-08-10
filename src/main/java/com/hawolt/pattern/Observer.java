package com.hawolt.pattern;

public interface Observer<T> {

    void dispatch(T t);
}
