package com.kayak.rpt.pub;


public interface ICallback<T,V> {

    T call(V v) throws Exception ;
}
