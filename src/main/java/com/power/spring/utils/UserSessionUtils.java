package com.power.spring.utils;

import com.power.spring.bean.UserSession;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenli on 2017/1/1.
 */
public class UserSessionUtils {

    public static final Short VALID_TIME = 100;
    private static final Map<String, UserSession> sessionIdMap = new HashMap<>();
    private static final Map<Long, UserSession> sessionUidMap = new HashMap<>();

    public static void addUserSession(UserSession us) {
        sessionIdMap.put(us.getSessionId(), us);
        sessionUidMap.put(us.getUserId(), us);
    }

    public static UserSession getSessionBySessionId(String sessionId) {
        return sessionIdMap.get(sessionId);
    }

    public static UserSession getSessionByUserId(long userId){
        return sessionUidMap.get(userId);
    }

    public static void deleteSession(String sessionId) {
        UserSession userSession = sessionIdMap.get(sessionId);
        long uid = userSession.getUserId();
        sessionIdMap.remove(sessionId);
        sessionUidMap.remove(uid);
    }

    public static void deleteSession(long userId) {
        UserSession userSession = sessionUidMap.get(userId);
        String sessionId = userSession.getSessionId();
        sessionIdMap.remove(sessionId);
        sessionUidMap.remove(userId);
    }
}
