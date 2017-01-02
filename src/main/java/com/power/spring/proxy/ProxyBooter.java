package com.power.spring.proxy;

import com.power.spring.annotations.MethodProxy;
import com.power.spring.annotations.PackageScanner;
import com.power.spring.utils.PropUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by shenli on 2017/1/3.
 */
public class ProxyBooter {


    public static class ProxyItem{
        private Class clazz;
        private String methodName;
        private Proxy proxy;

        public ProxyItem(Class clazz, String methodName, Proxy proxy) {
            this.clazz = clazz;
            this.methodName = methodName;
            this.proxy = proxy;
        }

        public Class getClazz() {
            return clazz;
        }

        public String getMethodName() {
            return methodName;
        }

        public Proxy getProxy() {
            return proxy;
        }
    }

    private static Map<String, ProxyItem> proxyItemHashMap = new HashMap<>();

    public static void init(){
        System.out.println("ProxyBooter.init");
        String scanPkg = PropUtils.getProp("proxy.scan.package");
//        System.out.println(" scanPkg = " + scanPkg);
        Set<Class<?>> allclasses = PackageScanner.findFileClass(scanPkg);
        for (Class clazz: allclasses) {
//            System.out.println(" .. scan clazz = " + clazz);
            if(Proxy.class.isAssignableFrom(clazz)){
                MethodProxy proxy = (MethodProxy)clazz.getAnnotation(MethodProxy.class);
                if(proxy != null) {
                    Class clz = proxy.clazz();
                    String methodName = proxy.methodName();
                    System.out.println(" .. ADD -> clazz = " + clz + ",methodName=" + methodName);
                    try {
                        Object instance = clazz.newInstance();
                        String key = clz.getName() + "-" + methodName;
                        System.out.println("key input = " + key);
                        proxyItemHashMap.put(key, new ProxyItem(clz, methodName, (Proxy) instance));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static ProxyItem getByClzMethod(Class clazz, String methodName) {
        System.out.println("ProxyBooter.getByClzMethod(clazz="+clazz+",methodName="+methodName+")");
        String key = clazz.getName() + "-" + methodName;
        System.out.println("key get = " + key);
        return proxyItemHashMap.get(key);
    }
}
