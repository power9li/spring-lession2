package com.power.learn.utils;

import com.power.spring.utils.JSONUtils;
import org.junit.Test;

/**
 * Created by shenli on 2017/1/2.
 */
public class TestJSONutils {

    @Test
    public void test(){
        long userId = 1;
        String json = JSONUtils.toJSON(userId);
        System.out.println("json = " + json);
    }
}
