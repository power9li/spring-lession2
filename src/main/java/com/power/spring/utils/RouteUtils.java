package com.power.spring.utils;

import com.power.spring.annotations.Controller;
import com.power.spring.annotations.Mapping;
import com.power.spring.annotations.PackageScanner;
import com.power.spring.route.Route;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by shenli on 2017/1/2.
 */
public class RouteUtils {


    private static Map<String, RouteItem> routeMap = new HashMap<>();

    static{
        String scanPkg = PropUtils.getProp("route.scan.package");
        System.out.println("scanPkg = " + scanPkg);
        Set<Class<?>> allclasses = PackageScanner.findFileClass(scanPkg);
        for (Class clazz: allclasses) {
            if(clazz.isAnnotationPresent(Controller.class)){
//                System.out.println("YES clazz = " + clazz);
                Method[] declaredMethods = clazz.getDeclaredMethods();
//                System.out.println(Arrays.toString(declaredMethods));
                for (Method method : declaredMethods) {
                    if(method.isAnnotationPresent(Mapping.class)){
//                        System.out.println("YES method = " + method);
                        Mapping mapping = method.getAnnotation(Mapping.class);
                        String command = mapping.value();
                        System.out.println("command = " + command + ",method=" + method.getName());
                        try {
                            Object instance = clazz.newInstance();
                            routeMap.put(command, new RouteItem(command,instance,method));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
    }

    public static Route getRoute(String command){
        Route route = null;
        RouteItem ri = routeMap.get(command);
        if (ri != null) {
            route = new Route(ri);
        }
        return route;
    }

    public static class RouteItem{
        private String command;
        private Object instance;
        private Method method;

        public RouteItem(String command, Object instance, Method method) {
            this.command = command;
            this.instance = instance;
            this.method = method;
        }

        public String getCommand() {
            return command;
        }

        public Object getInstance() {
            return instance;
        }

        public Method getMethod() {
            return method;
        }
    }


    public static void main(String[] args) {

    }
}
