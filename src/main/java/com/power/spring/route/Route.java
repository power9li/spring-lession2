package com.power.spring.route;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.power.spring.enums.EventType;
import com.power.spring.enums.StatusCode;
import com.power.spring.event.EventBooter;
import com.power.spring.protocol.Response;
import com.power.spring.proxy.ProxyBooter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenli on 2017/1/1.
 */
public class Route {

    private String command;
    private String serviceName;
    private String methodName;
    private EventType[] eventTypes;

    private Object instance;
    private Method method;


    public Route(String command, String serviceName, String methodName,EventType[] eventTypes) {
        this.command = command;
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.eventTypes = eventTypes;
    }

    public Route(RouteBooter.RouteItem item) {
        this.command = item.getCommand();
        this.instance = item.getInstance();
        this.method = item.getMethod();
        this.eventTypes = item.getEventTypes();
    }


    private void preInstanceMethod() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
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

    }

    private Object proxyOrEventInvoke(Method method, Object instance, Object[] params) throws InvocationTargetException, IllegalAccessException{
        Object rst = null;

        List<EventBooter.ListenerItem> preEvents = new ArrayList<>();
        List<EventBooter.ListenerItem> afterEvents = new ArrayList<>();
        for (EventType et : eventTypes) {
            EventBooter.ListenerItem item = EventBooter.getByCommandEventType(command, et);
            if (et.equals(EventType.PRE_INVOKE)) {
                preEvents.add(item);
            } else if (et.equals(EventType.AFTER_INVOKE)) {
                afterEvents.add(item);
            }
        }

        ProxyBooter.ProxyItem proxyItem = ProxyBooter.getByClzMethod(instance.getClass(), method.getName());


        //pre events
        for (EventBooter.ListenerItem preEvent : preEvents) {
            preEvent.getMethod().invoke(preEvent.getInstance(), params);
        }

        //proxy invoke or real invoke
        if(proxyItem != null){
            rst = proxyItem.getProxy().proxy(instance, method, params);
        }
        else {
            rst = method.invoke(instance, params);
        }

        //after events
        for(EventBooter.ListenerItem afterEvent : afterEvents){
            afterEvent.getMethod().invoke(afterEvent.getInstance(),new Object[]{params,rst});
        }

        return rst;
    }

    public Response handle(String jsonBody) {
        Response resp = new Response();
        Object rst = null;
        try {

            preInstanceMethod();

            int paramCount = method.getParameterCount();
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] params = null;

            //没有参数,直接调用
//        if (paramCount == 0 ) {
//            rst = method.invoke(instance, null);
//        }
            //单个参数,直接映射Json到参数
        /*else*/
            if (paramCount == 1) {
                Object oneParam = new Gson().fromJson(jsonBody, parameterTypes[0]);
                params = new Object[]{oneParam};
//            rst = method.invoke(instance, oneParam);
            }
            //多个参数映射json到json数组,再到参数列表
            else if(paramCount > 1){
                params = new Object[paramCount];
                JsonParser parser = new JsonParser();
                JsonArray jsonArray = parser.parse(jsonBody).getAsJsonArray();
                Gson gson = new Gson();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonElement je = jsonArray.get(i);
                    params[i] = gson.fromJson(je,parameterTypes[i]);
                    System.out.println("params["+i+"] = " + params[i]);
//                rst = method.invoke(instance, params);
                }
            }
            rst = proxyOrEventInvoke(method, instance, params);

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

    public EventType[] getEventTypes() {
        return eventTypes;
    }
}
