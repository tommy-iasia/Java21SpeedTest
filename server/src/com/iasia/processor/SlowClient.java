package com.iasia.processor;

import java.util.HashMap;

public class SlowClient {

    public SlowClient(int code, int balance) {
        this.code = code;
        this.balance = balance;
    }

    public final int code;

    public int balance;
    public final HashMap<Integer, Integer> holdings = new HashMap<>();
}
