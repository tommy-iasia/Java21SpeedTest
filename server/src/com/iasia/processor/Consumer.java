package com.iasia.processor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Consumer implements AutoCloseable {

    private Socket client;
    private Socket server;

    public static Consumer create() throws IOException {
        final var consumer = new Consumer();

        try (final var server = new ServerSocket(8081)) {
            new Thread(() -> {
                try {
                    consumer.server = server.accept();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                var buffer = new byte[1024 * 1024];
                try {
                    var stream = consumer.server.getInputStream();
                    while (stream.read(buffer) != -1) {
                        buffer[0] = 1;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            consumer.client = new Socket("localhost", 8081);
        }

        return consumer;
    }

    public void write(String text) throws IOException {
        client.getOutputStream().write(text.getBytes());
    }

    @Override
    public void close() throws Exception {
        client.close();
        server.close();
    }
}
