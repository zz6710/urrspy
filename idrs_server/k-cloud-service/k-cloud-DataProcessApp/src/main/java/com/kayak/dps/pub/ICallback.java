package com.kayak.dps.pub;

public interface ICallback<T> {
    void call(T t) throws Exception ;
}
