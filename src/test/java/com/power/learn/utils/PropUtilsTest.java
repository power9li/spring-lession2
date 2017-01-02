package com.power.learn.utils;

import com.power.spring.utils.PropUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by shenli on 2017/1/1.
 */
public class PropUtilsTest {

    @Test
    public void testGetProp(){
        String p = PropUtils.getProp("user.file.path");
        Assert.assertEquals("/tmp/spring-lesson2/",p);
    }
}
