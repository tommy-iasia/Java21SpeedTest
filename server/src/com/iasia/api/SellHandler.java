package com.iasia.api;

import com.iasia.processor.SellResult;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.LinkedList;

public class SellHandler implements HttpHandler {

    public SellHandler(Processor processor) {
        this.processor = processor;
    }

    private final Processor processor;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var input = BuyHandler.loadInput(exchange);
        if (input == null) {
            return;
        }

        var results = new LinkedList<ProcessResult<SellResult>>();

        for (int client = input.clientFrom; client <= input.clientTo; client++) {
            ProcessResult<SellResult> result = processor
                    .sell(client, input.product, input.quantity, input.price);

            results.add(result);
        }

        BuyHandler.save(results, "sell");

        BuyHandler.response(exchange, results);
    }
}
