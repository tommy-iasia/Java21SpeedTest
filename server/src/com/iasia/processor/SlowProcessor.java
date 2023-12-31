package com.iasia.processor;

import java.io.IOException;
import java.util.HashMap;

public class SlowProcessor implements Processor {

    public SlowProcessor(Consumer consumer) {
        this.consumer = consumer;
    }

    private final HashMap<Integer, SlowClient> clients = new HashMap<>();
    private final Consumer consumer;

    @Override
    public void client(int code, int balance) {
        var client = clients.get(code);
        if (client != null) {
            client.balance += balance;
        } else {
            clients.put(code, new SlowClient(code, balance));
        }
    }

    @Override
    public BuyResult buy(int clientCode, int productCode, int quantity, int price) {
        var client = clients.get(clientCode);
        if (client == null) {
            return BuyResult.CLIENT_NOT_FOUND;
        }

        var amount = price * quantity;
        if (client.balance < amount) {
            return BuyResult.INSUFFICIENT_FUNDS;
        }

        client.balance -= amount;

        client.holdings.put(productCode,
                client.holdings.getOrDefault(productCode, 0) + quantity);

        try {
            consumer.write("Client " + clientCode
                    + " bought " + quantity
                    + " of " + productCode
                    + " at " + price + ".");
        } catch (IOException e) {
            return BuyResult.CONSUMER_ERROR;
        }

        return BuyResult.SUCCESS;
    }

    @Override
    public SellResult sell(int clientCode, int productCode, int quantity, int price) {
        var client = clients.get(clientCode);
        if (client == null) {
            return SellResult.CLIENT_NOT_FOUND;
        }

        if (client.holdings.getOrDefault(productCode, 0) < quantity) {
            return SellResult.INSUFFICIENT_STOCKS;
        }

        client.holdings.put(productCode,
                client.holdings.getOrDefault(productCode, 0) - quantity);

        client.balance += quantity * price;

        try {
            consumer.write("Client " + clientCode
                    + " sold " + quantity
                    + " of " + productCode
                    + " at " + price + ".");
        } catch (IOException e) {
            return SellResult.CONSUMER_ERROR;
        }

        return SellResult.SUCCESS;
    }
}
