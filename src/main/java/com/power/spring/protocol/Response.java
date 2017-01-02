package com.power.spring.protocol;

import com.power.spring.enums.StatusCode;

/**
 * Created by shenli on 2017/1/1.
 */
public class Response {

    private StatusCode status;
    private String respBody;

    public Response(){}

    public Response(StatusCode status, String respBody) {
        this.status = status;
        this.respBody = respBody;
    }

    public StatusCode getStatus() {
        return status;
    }

    public void setStatus(StatusCode status) {
        this.status = status;
    }

    public String getRespBody() {
        return respBody;
    }

    public void setRespBody(String respBody) {
        this.respBody = respBody;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", respBody='" + respBody + '\'' +
                '}';
    }
}
