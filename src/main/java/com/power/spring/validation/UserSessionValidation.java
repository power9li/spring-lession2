package com.power.spring.validation;

import com.power.spring.bean.UserSession;
import com.power.spring.protocol.Request;
import com.power.spring.protocol.Validation;
import com.power.spring.utils.UserSessionUtils;
import com.sun.net.httpserver.Headers;

/**
 * Created by shenli on 2017/1/1.
 */
public class UserSessionValidation implements Validation {

    @Override
    public boolean check(Request request) {

        Headers headers = request.getHeaders();
        if(headers != null && headers.containsKey("sessionId")){
            String sessionId = headers.getFirst("sessionId");
            UserSession session = UserSessionUtils.getSessionBySessionId(sessionId);
            if (session != null) {
                if(session.isValid()) {
                    return true;
                }
                else{
                    //TODO 为了简化,在请求时删除过期session,实际上应该由后台定时任务执行
                    UserSessionUtils.deleteSession(sessionId);
                }
            }
        }
        return false;
    }
}
