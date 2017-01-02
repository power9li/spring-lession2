package com.power.spring.route;

import com.power.spring.protocol.Request;
import com.power.spring.protocol.Response;
import com.power.spring.protocol.StatusCode;
import com.power.spring.protocol.Validation;
import com.power.spring.trans.RequestParser;
import com.power.spring.utils.RouteUtils;
import com.power.spring.validation.UserSessionValidation;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenli on 2017/1/2.
 */
public class RequestDispatcher implements HttpHandler {

    private static final Map<String, Route> routeMap = new HashMap<>();
    private static final Map<Collection<String>, Validation> validationMap = new HashMap<>();

    static {
        validationMap.put(Arrays.asList(new String[]{"user/create", "user/delete,", "user/disable", "user/queryUsers"}),new UserSessionValidation());
    }


    private Response preHandle(Request request){
        System.out.println("RequestDispatcher.preHandle");
        String command = request.getCommand();

        for (Map.Entry<Collection<String>, Validation> entries : validationMap.entrySet()) {
            Collection<String> validaCommands = entries.getKey();
            if (validaCommands.contains(command)) {
                Validation validation = entries.getValue();
                boolean passed = validation.check(request);
                if (!passed) {
                    Response resp = new Response();
                    resp.setStatus(StatusCode.SESSION_TIMEOUT);
                    return resp;
                }
            }
        }
        return null;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("RequestDispatcher.handle");

        Request request = RequestParser.parse(httpExchange);
        String command = request.getCommand();
        System.out.println("command = " + command);
        Response resp = null;

        //pre Validation
        resp = preHandle(request);
        System.out.println("preHandle resp = " + resp);
        if (resp != null) {
            wrapperResponse(resp,httpExchange);
            return;
        }

        System.out.println("before doHandler");
        //do hanlder
//        Route route = routeMap.get(command);
        Route route = RouteUtils.getRoute(command);
        System.out.println("route = " + route);
        if (route != null) {
            resp = route.handle(request.getReqJsonBody());
            wrapperResponse(resp, httpExchange);
            return;
        }

        wrapperResponse(new Response(),httpExchange);
    }

    public void wrapperResponse(Response resp, HttpExchange httpExchange)  {
        System.out.println("RequestDispatcher.wrapperResponse");
        try {
            String response = resp.getRespBody();
            System.out.println("response = " + response);
            if (response == null) {
                response = "";
            }
            int stateCode = (resp.getStatus() == null) ? 500 : resp.getStatus().getCode();
            System.out.println("Server HTTP stateCode = " + stateCode);
            httpExchange.sendResponseHeaders(stateCode, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }


}
