package com.power.spring.client;

import com.power.spring.bean.User;
import com.power.spring.protocol.Request;
import com.power.spring.protocol.Response;
import com.power.spring.enums.StatusCode;
import com.power.spring.utils.HexUtils;
import com.power.spring.utils.JSONUtils;
import com.power.spring.utils.MD5Utils;

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

        //构造用户对象
        User user = new User();
        user.setEnabled(true);
        user.setUserName("Jack");
        user.setPassword(HexUtils.byte2hex(MD5Utils.getMD5("1234567")));
        //序列化
        String reqJsonBody = JSONUtils.toJSON(user);
        //创建请求对象
        Request req = new Request("user/create",reqJsonBody);
        //调用服务
        Response resp = HttpClientWrapper.doRequest(req);
        System.out.println("resp.getStatus() = " + resp.getStatus());
        System.out.println("resp.getRespBody() = " + resp.getRespBody());


        user.setUserName("Jack");
        user.setPassword(HexUtils.byte2hex(MD5Utils.getMD5("1234567")));
        reqJsonBody = JSONUtils.toJSON(user);
        req = new Request("user/create", reqJsonBody);
        resp = HttpClientWrapper.doRequest(req);
        System.out.println("resp.getStatus() = " + resp.getStatus());
        System.out.println("resp.getRespBody() = " + resp.getRespBody());


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
