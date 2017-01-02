package com.power.spring.proxy.impl;

import com.power.spring.annotations.MethodProxy;
import com.power.spring.bean.User;
import com.power.spring.dao.UserDao;
import com.power.spring.dao.impl.UserDaoByFile;
import com.power.spring.proxy.Proxy;
import com.power.spring.service.impl.UserServiceImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by shenli on 2017/1/3.
 */
@MethodProxy(clazz = UserServiceImpl.class,methodName = "createUser")
public class CreateUserProxy implements Proxy {

    private UserDao userDao = new UserDaoByFile();

    @Override
    public Object proxy(Object sourceTarget, Method sourceMethod, Object[] sourceParams)
            throws InvocationTargetException, IllegalAccessException {

        if(sourceParams.length ==1){
            User u = (User) sourceParams[0];
            boolean hasSeamUserName = userDao.hasSeamUserName(u.getUserName());
            if (hasSeamUserName) {
                throw new RuntimeException(
                        "create user failed, username ["+u.getUserName()+"]already existed.");
            }
        }
        return sourceMethod.invoke(sourceTarget, sourceParams);
    }
}
