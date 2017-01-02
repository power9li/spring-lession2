package com.power.spring.enums;

/**
 * Created by shenli on 2017/1/1.
 */
public enum StatusCode {

    NORMAL(200),
    SERVER_INTERNAL_ERROR(500),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    SESSION_TIMEOUT(440),
    VALIDATION_FAILED(510);


    private int code;

    private StatusCode(int code){
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }

    public static StatusCode codeOf(int code){
        for (StatusCode sc : values()) {
            if (sc.getCode() == code) {
                return sc;
            }
        }
        return null;
    }

}
