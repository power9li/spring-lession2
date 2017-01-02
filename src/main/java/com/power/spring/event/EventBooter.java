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
    }

    private static Map<String, ListenerItem> listenerItemMap = new HashMap<>();

    public static void init(){
        System.out.println("EventBooter.init");
        String scanPkg = PropUtils.getProp("event.scan.package");
        System.out.println("scanPkg = " + scanPkg);
        Set<Class<?>> allclasses = PackageScanner.findFileClass(scanPkg);
        for (Class clazz: allclasses) {
//            if(clazz.isAnnotationPresent(Controller.class)){
//                System.out.println("YES clazz = " + clazz);
                Method[] declaredMethods = clazz.getDeclaredMethods();
//                System.out.println(Arrays.toString(declaredMethods));
                for (Method method : declaredMethods) {
                    if(method.isAnnotationPresent(EventListener.class)){
//                        System.out.println("YES method = " + method);
                        EventListener listener = method.getAnnotation(EventListener.class);
                        String command = listener.command();
                        EventType eventType = listener.event();
                        System.out.println("command = " + command + ",event=" + eventType + ",method=" + method.getName());
                        try {
                            Object instance = clazz.newInstance();
                            listenerItemMap.put(command+"-"+eventType, new ListenerItem(command,eventType,instance,method));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
//            }
        }
    }

    public static ListenerItem getByCommandEventType(String command, EventType eventType) {
        return listenerItemMap.get(command + "-" + eventType);
    }
}
