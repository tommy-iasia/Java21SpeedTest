package com.iasia;

import com.iasia.api.Server;
import com.iasia.api.Processor;
import com.iasia.processor.Consumer;
import com.iasia.processor.FastProcessor;
import com.iasia.processor.SlowProcessor;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        var consumer = Consumer.create();
        var innerProcessor = new SlowProcessor(consumer);
        var outerProcessor = new Processor(innerProcessor);

        Server.start(outerProcessor);

        System.out.println("Hello world!");
    }
}