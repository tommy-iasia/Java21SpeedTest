package com.iasia.api;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    public static HttpServer start(Processor processor) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", new HelloHandler());
        server.createContext("/client", new ClientHandler(processor));
        server.createContext("/buy", new BuyHandler(processor));
        server.createContext("/sell", new SellHandler(processor));

        server.start();

        return server;
    }
}
