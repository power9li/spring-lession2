package com.power.spring.trans;

import com.power.spring.protocol.Request;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by shenli on 2017/1/2.
 */
public class RequestParser {

    public static Request parse(HttpExchange httpExchange) {
        Request request = new Request();
        String command = httpExchange.getRequestURI().getPath();
        InputStream in = httpExchange.getRequestBody();
        String jsonBody = getInputContent(in);
        Headers hds = httpExchange.getRequestHeaders();
        request.setHeaders(hds);
        request.setCommand(command);
        request.setReqJsonBody(jsonBody);
        return request;
    }

    private static String getInputContent(InputStream is){
        String body = null;
        try {
            byte[] bytes = new byte[1024 * 1024];

            int nRead = 1;
            int nTotalRead = 0;
            while (nRead > 0) {
                nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
                if (nRead > 0)
                    nTotalRead = nTotalRead + nRead;
            }
            body = new String(bytes, 0, nTotalRead, "utf-8");
            System.out.println("body = " + body);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return body;
    }
}
