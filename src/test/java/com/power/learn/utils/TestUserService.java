package com.power.learn.utils;

import com.power.spring.bean.UserSession;
import com.power.spring.service.UserService;
import com.power.spring.service.impl.UserServiceImpl;
import org.junit.Test;

/**
 * Created by shenli on 2017/1/1.
 */
public class TestUserService {

    private UserService userService = new UserServiceImpl();

    @Test
    public void testUserServiceLogin(){
        UserSession userSession = userService.login("李四", "12345");
        String sessionId = userSession.getSessionId();
        System.out.println(sessionId != null);
        System.out.println("sessionId = " + sessionId);


    }
}
