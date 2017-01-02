package com.power.spring.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by shenli on 2017/1/1.
 */
public class PropUtils {

    private static Properties prop = new Properties();
    static{
        try {
            prop.load(PropUtils.class.getResourceAsStream("/common.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getProp(String key) {
        return prop.getProperty(key);
    }

}
