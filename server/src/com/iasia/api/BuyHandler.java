package com.iasia.api;

import com.iasia.processor.BuyResult;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class BuyHandler implements HttpHandler {

    public BuyHandler(Processor processor) {
        this.processor = processor;
    }

    private final Processor processor;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var input = loadInput(exchange);
        if (input == null) {
            return;
        }

        var results = new LinkedList<ProcessResult<BuyResult>>();

        for (int client = input.clientFrom; client <= input.clientTo; client++) {
            ProcessResult<BuyResult> result = processor
                    .buy(client, input.product, input.quantity, input.price);

            results.add(result);
        }

        response(exchange, results);
    }

    public static BuyInput loadInput(HttpExchange exchange) throws IOException {
        var clientFrom = ClientHandler.loadInputValue(exchange, "clientFrom");
        if (clientFrom == null) {
            return null;
        }

        var clientTo = ClientHandler.loadInputValue(exchange, "clientTo");
        if (clientTo == null) {
            return null;
        }

        var product = ClientHandler.loadInputValue(exchange, "product");
        if (product == null) {
            return null;
        }

        var quantity = ClientHandler.loadInputValue(exchange, "quantity");
        if (quantity == null) {
            return null;
        }

        var price = ClientHandler.loadInputValue(exchange, "price");
        if (price == null) {
            return null;
        }

        var input = new BuyInput();
        input.clientFrom = clientFrom;
        input.clientTo = clientTo;
        input.product = product;
        input.quantity = quantity;
        input.price = price;

        return input;
    }

    public static <T> void response(HttpExchange exchange, List<ProcessResult<T>> results) throws IOException {
        long sum = 0;

        for (ProcessResult<T> result : results) {
            sum += result.elapsed;
        }

        long mean = sum / results.size();

        HelloHandler.response(exchange, 200,
                "total count: " + results.size() + "\r\n"
                        + "total elapsed: " + (sum / 1_000_000) + "ms\r\n"
                        + "mean elapsed: " + mean + "ns");
    }
}
