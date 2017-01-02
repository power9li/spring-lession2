package com.power.spring.client;

import com.power.spring.bean.User;
import com.power.spring.protocol.Request;
import com.power.spring.protocol.Response;
import com.power.spring.enums.StatusCode;
import com.power.spring.utils.JSONUtils;

/**
 * Created by shenli on 2016/12/31.
 */
public class UserClient {

//    private static SimpleHttpServer server ;

    public static void main(String[] args) {

        doCreateUser();
//        doDisableUser();
//        doQueryUser();


//        server.destory();

    }

    public static void doCreateUser(){

        User user = new User();
        user.setEnabled(true);
        user.setUserName("李四");
        user.setPassword("1234567");
        String reqJsonBody = JSONUtils.toJSON(user);
        System.out.println("reqJsonBody = " + reqJsonBody);
        Request req = new Request("user/create",reqJsonBody);
//        Response resp = server.handle(req);
        Response resp = HttpClientWrapper.doRequest(req);
        System.out.println("resp.getStatus() = " + resp.getStatus());

        user.setUserName("王五");
        user.setPassword("1qaz2wsx");
        reqJsonBody = JSONUtils.toJSON(user);
        System.out.println("resp = " + resp);
        req = new Request("user/create", reqJsonBody);
//        resp = server.handle(req);
        resp = HttpClientWrapper.doRequest(req);
        System.out.println("resp.getStatus() = " + resp.getStatus());



    }

    public static void doDisableUser(){
        long userId = 5;
        String reqJsonBody = JSONUtils.toJSON(userId);
        Request req = new Request("user/disable", reqJsonBody);
//        Response resp = server.handle(req);
        Response resp = HttpClientWrapper.doRequest(req);
        System.out.println("resp.getStatus() = " + resp.getStatus());

    }

    public static void doQueryUser(){
        String userName = "李四";
        boolean onlyValidUser = true;
        String reqJsonBody = JSONUtils.toJSON(userName, onlyValidUser);
        System.out.println("reqJsonBody = " + reqJsonBody);
        Request req = new Request("user/queryUsers", reqJsonBody);
//        Response resp = server.handle(req);
        Response resp = HttpClientWrapper.doRequest(req);
        System.out.println("resp.getStatus() = " + resp.getStatus());
        if (resp.getStatus().equals(StatusCode.NORMAL)) {
            String respBody = resp.getRespBody();
            System.out.println("respBody = " + respBody);
        }
    }
}
