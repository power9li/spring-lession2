package com.power.spring.server;

import com.power.spring.event.EventBooter;
import com.power.spring.proxy.ProxyBooter;
import com.power.spring.route.RequestDispatcher;
import com.power.spring.route.RouteBooter;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by shenli on 2016/12/31.
 */
public class SimpleHttpServer {

    public void start() {
        init();
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.createContext("/", new RequestDispatcher());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("server started. listen port 8000");
    }

    private void init(){
        EventBooter.init();
        ProxyBooter.init();
        RouteBooter.init();
    }
    public static void main(String[] args) {
        new SimpleHttpServer().start();
    }
}
