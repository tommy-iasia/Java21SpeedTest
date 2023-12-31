package com.iasia.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HelloHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        response(exchange, 200, "Hello world!");
    }

    public static void response(HttpExchange exchange, int statusCode, String bodyText) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");

        exchange.sendResponseHeaders(statusCode, bodyText.length());
        exchange.getResponseBody().write(bodyText.getBytes());

        exchange.close();
    }
}
