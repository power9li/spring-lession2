package com.power.spring.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * Created by shenli on 2017/1/1.
 */
public class JSONUtils {

    public static String toJSON(Object pojo) {
        Gson gson = new Gson();
        return gson.toJson(pojo);
    }

    public static String toJSON(Object ... args) {
        Gson gson = new Gson();
        JsonArray jary = new JsonArray();
        for (Object o : args) {
            JsonElement je = gson.toJsonTree(o);
            jary.add(je);
        }
        return gson.toJson(jary);
    }
}
