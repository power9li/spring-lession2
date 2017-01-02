package com.power.spring.protocol;

import com.sun.net.httpserver.Headers;

/**
 * Created by shenli on 2017/1/1.
 */
public class Request {

    private Headers headers;
    private String command;
    private String reqJsonBody;

    public Request(){

    }

    public Request(String command, String reqJsonBody){
        this.command = command;
        this.reqJsonBody = reqJsonBody;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getReqJsonBody() {
        return reqJsonBody;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public void setReqJsonBody(String reqJsonBody) {
        this.reqJsonBody = reqJsonBody;
    }
}
