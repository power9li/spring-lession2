package com.power.spring.route;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.power.spring.protocol.Response;
import com.power.spring.protocol.StatusCode;
import com.power.spring.utils.RouteUtils;

import java.lang.reflect.Method;

/**
 * Created by shenli on 2017/1/1.
 */
public class Route {

    private String command;
    private String serviceName;
    private String methodName;

    private Object instance;
    private Method method;


    public Route(String command, String serviceName, String methodName) {
        this.command = command;
        this.serviceName = serviceName;
        this.methodName = methodName;
    }

    public Route(RouteUtils.RouteItem item) {
        this.command = item.getCommand();
        this.instance = item.getInstance();
        this.method = item.getMethod();
    }

    public Response handle(String jsonBody) {
        Response resp = new Response();
        Object rst = null;
        try {
            if(instance == null) {
                instance = Class.forName(serviceName).newInstance();
            }
            if(method == null) {
                Method[] ms = instance.getClass().getDeclaredMethods();
                for (Method method2 : ms) {
                    if (method2.getName().equals(methodName)){
                        method = method2;
                        break;
                    }
                }
            }
            if (method == null) {
                throw new RuntimeException("can not match method by name:" + methodName);
            }

            int paramCount = method.getParameterCount();
            Class<?>[] parameterTypes = method.getParameterTypes();

            //没有参数,直接调用
            if (paramCount == 0 ) {
                rst = method.invoke(instance, null);
            }
            //单个参数,直接映射Json到参数
            else if (paramCount == 1) {
                Object oneParam = new Gson().fromJson(jsonBody, parameterTypes[0]);
                rst = method.invoke(instance, oneParam);
            }
            //多个参数映射json到json数组,再到参数列表
            else{
                Object[] params = new Object[paramCount];
                JsonParser parser = new JsonParser();
                JsonArray jsonArray = parser.parse(jsonBody).getAsJsonArray();
                Gson gson = new Gson();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonElement je = jsonArray.get(i);
                    params[i] = gson.fromJson(je,parameterTypes[i]);
                    System.out.println("params["+i+"] = " + params[i]);
                }
                rst = method.invoke(instance, params);
            }

            resp.setStatus(StatusCode.NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(StatusCode.SERVER_INTERNAL_ERROR);
            resp.setRespBody(e.getMessage());
        }
        if(rst != null) {
            Gson gson = new Gson();
            String respBody = gson.toJson(rst);
            resp.setRespBody(respBody);
        }
        return resp;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
