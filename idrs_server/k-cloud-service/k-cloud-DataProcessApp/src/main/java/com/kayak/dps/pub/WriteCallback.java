package com.kayak.dps.pub;

import java.util.List;

public interface WriteCallback<T> {

    List<T> call(Object obj) throws Exception ;
}
