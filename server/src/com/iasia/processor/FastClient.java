package com.iasia.processor;

import java.util.HashMap;

public class FastClient {

    public FastClient(int code, int balance) {
        this.code = code;
        this.balance = balance;
    }

    public final int code;

    public int balance;
    public final int[] holdings = new int[10];
}
