package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import org.example.service.LoginServiceImpl;
import org.example.service.SignUpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ShoppingCartServer {

    @Autowired
    private LoginServiceImpl loginService;
    @Autowired
    private SignUpServiceImpl signUpService;

    @PostConstruct
    public void init()  throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8081)
                .addService(loginService)
                .addService(signUpService)
                .build();
        System.out.println("Starting server...");
        server.start();
        System.out.println("Server started!");
        server.awaitTermination();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(ShoppingCartServer.class, args);
    }

}
