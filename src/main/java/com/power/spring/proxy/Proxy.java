package com.power.spring.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by shenli on 2017/1/3.
 */
public interface Proxy {

    public Object proxy(Object sourceTarget, Method sourceMethod, Object[] sourceParams)
            throws InvocationTargetException, IllegalAccessException;
}
