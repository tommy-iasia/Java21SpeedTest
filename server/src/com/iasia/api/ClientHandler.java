package com.iasia.api;

import com.iasia.processor.BuyResult;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientHandler implements HttpHandler {

    public ClientHandler(Processor processor) {
        this.processor = processor;
    }

    private final Processor processor;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var input = loadInput(exchange);
        if (input == null) {
            return;
        }

        for (int client = input.clientFrom; client <= input.clientTo; client++) {
            processor.client(client, input.balance);
        }

        HelloHandler.response(exchange, 200, "done");
    }

    private static ClientInput loadInput(HttpExchange exchange) throws IOException {
        Integer clientFrom = loadInputValue(exchange, "clientFrom");
        if (clientFrom == null) {
            return null;
        }

        Integer clientTo = loadInputValue(exchange, "clientTo");
        if (clientTo == null) {
            return null;
        }

        Integer balance = loadInputValue(exchange, "balance");
        if (balance == null) {
            return null;
        }

        ClientInput input = new ClientInput();
        input.clientFrom = clientFrom;
        input.clientTo = clientTo;
        input.balance = balance;

        return input;
    }
    public static Integer loadInputValue(HttpExchange exchange, String name) throws IOException {
        String query = exchange.getRequestURI().getQuery();

        Matcher matcher = Pattern.compile(name + "=(\\d+)").matcher(query);
        if (!matcher.find()) {
            HelloHandler.response(exchange, 400, "Missing " + name);
            return null;
        }

        String text = matcher.group(1);

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            HelloHandler.response(exchange, 400, "Invalid " + name);
            return null;
        }
    }
}
