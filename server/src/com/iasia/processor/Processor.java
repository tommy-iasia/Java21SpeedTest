package com.iasia.processor;

public interface Processor {

    void client(int code, int balance);

    BuyResult buy(int clientCode, int productCode, int quantity, int price);

    SellResult sell(int clientCode, int productCode, int quantity, int price);
}
