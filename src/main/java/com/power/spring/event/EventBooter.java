package com.power.spring.event;

import com.power.spring.annotations.EventListener;
import com.power.spring.annotations.PackageScanner;
import com.power.spring.enums.EventType;
import com.power.spring.utils.PropUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by shenli on 2017/1/2.
 */
public class EventBooter {

    public static class ListenerItem{
        private String command;
        private EventType eventType;
        private Object instance;
        private Method method;

        public ListenerItem(String command, EventType eventType, Object instance, Method method) {
            this.command = command;
            this.eventType = eventType;
            this.instance = instance;
            this.method = method;
        }

        public String getCommand() {
            return command;
        }

        public EventType getEventType() {
            return eventType;
        }

        public Object getInstance() {
            return instance;
        }

        public Method getMethod() {
            return method;
        }

        @Override
        public String toString() {
            return "ListenerItem{" +
                    "command='" + command + '\'' +
                    ", eventType=" + eventType +
                    ", instance=" + instance +
                    ", method=" + method +
                    '}';
        }
    }

    private static Map<String, ListenerItem> listenerItemMap = new HashMap<>();

    public static void init(){
        System.out.println("EventBooter.init");
        String scanPkg = PropUtils.getProp("event.scan.package");
        System.out.println(" scanPkg = " + scanPkg);
        Set<Class<?>> allclasses = PackageScanner.findFileClass(scanPkg);
        for (Class clazz: allclasses) {
//            System.out.println(" .. scan clazz = " + clazz);
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if(method.isAnnotationPresent(EventListener.class)){
                    EventListener listener = method.getAnnotation(EventListener.class);
                    String command = listener.command();
                    EventType eventType = listener.event();
                    System.out.println(" .. ADD -> command = " + command + ", event=" + eventType.name() + ", method=" + method.getName());
                    try {
                        Object instance = clazz.newInstance();
                        listenerItemMap.put(command+"-"+eventType, new ListenerItem(command,eventType,instance,method));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public static ListenerItem getByCommandEventType(String command, EventType eventType) {
        System.out.println("EventBooter.getByCommandEventType(command="+command+",enentType="+eventType.name()+")");
        return listenerItemMap.get(command + "-" + eventType);
    }
}
