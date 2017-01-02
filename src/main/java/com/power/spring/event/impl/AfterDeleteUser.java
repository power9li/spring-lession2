package com.power.spring.event.impl;

import com.power.spring.annotations.EventListener;
import com.power.spring.enums.EventType;
import com.power.spring.utils.UserSessionUtils;

/**
 * Created by shenli on 2017/1/3.
 */
public class AfterDeleteUser {

    @EventListener(command = "/users/delete" ,event = EventType.AFTER_INVOKE)
    public void afterDeleteUser(Object[] params, boolean isDeleted){
        if (isDeleted) {
            long userId = (Long) params[0];
            UserSessionUtils.deleteSession(userId);
            System.out.println("delete User session (userId = " + userId+")");
        }
    }
}
