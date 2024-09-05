package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.example.service.LoginServiceImpl;

import java.io.IOException;

public class ShoppingCartServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8081)
                .addService(new LoginServiceImpl()).build();

        System.out.println("Starting server...");
        server.start();
        System.out.println("Server started!");
        server.awaitTermination();
    }
}
