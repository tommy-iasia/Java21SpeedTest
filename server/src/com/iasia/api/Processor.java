package com.iasia.api;

import com.iasia.processor.BuyResult;
import com.iasia.processor.SellResult;

public class Processor {

    public Processor(com.iasia.processor.Processor handler) {
        this.handler = handler;
    }

    private final com.iasia.processor.Processor handler;

    public void client(int code, int balance) {
        handler.client(code, balance);
    }

    public ProcessResult<BuyResult> buy(int clientCode, int productCode, int quantity, int price) {
        var startTime = System.nanoTime();

        var result = handler.buy(clientCode, productCode, quantity, price);

        var endTime = System.nanoTime();

        return new ProcessResult<>(result, endTime - startTime);
    }

    public ProcessResult<SellResult> sell(int clientCode, int productCode, int quantity, int price) {
        var startTime = System.nanoTime();

        var result = handler.sell(clientCode, productCode, quantity, price);

        var endTime = System.nanoTime();

        return new ProcessResult<>(result, endTime - startTime);
    }
}
