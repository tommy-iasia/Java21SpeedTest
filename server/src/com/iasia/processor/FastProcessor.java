package com.iasia.processor;

import java.io.IOException;

public class FastProcessor implements Processor {

    public FastProcessor(Consumer consumer) {
        this.consumer = consumer;
    }

    private FastClient[] clients = new FastClient[0];
    private final Consumer consumer;

    @Override
    public void client(int code, int balance) {
        if (code > clients.length) {
            var newClients = new FastClient[code];
            System.arraycopy(clients, 0, newClients, 0, clients.length);

            for (int i = clients.length; i < newClients.length; i++) {
                newClients[i] = new FastClient(i, 0);
            }

            clients = newClients;
        }

        for (FastClient client : clients) {
            client.balance += balance;
        }
    }

    @Override
    public BuyResult buy(int clientCode, int productCode, int quantity, int price) {
        if (clientCode < 0 || clientCode >= clients.length) {
            return BuyResult.CLIENT_NOT_FOUND;
        }

        var client = clients[clientCode];

        int amount = price * quantity;
        if (client.balance < amount) {
            return BuyResult.INSUFFICIENT_FUNDS;
        }

        client.balance -= amount;

        if (productCode < 0 || productCode >= client.holdings.length) {
            return BuyResult.PRODUCT_NOT_FOUND;
        }

        client.holdings[productCode] += quantity;

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
        if (clientCode < 0 || clientCode >= clients.length) {
            return SellResult.CLIENT_NOT_FOUND;
        }

        var client = clients[clientCode];

        if (productCode < 0 || productCode >= client.holdings.length) {
            return SellResult.PRODUCT_NOT_FOUND;
        }

        var holding = client.holdings[productCode];

        if (holding < quantity) {
            return SellResult.INSUFFICIENT_STOCKS;
        }

        client.holdings[productCode] -= holding - quantity;

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
