package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.example.grpc.SignUpServiceGrpcImpl;
import org.example.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class ShoppingCartServer {

    @Autowired
    private LoginService loginService;
    @Autowired
    private SignUpServiceGrpcImpl signUpService;

    private ExecutorService serverDestroyer;

    @PostConstruct
    public void init()  throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8081)
                .addService(loginService)
                .addService(signUpService)
                .build();
        System.out.println("Starting server...");
        server.start();
        System.out.println("Server started!");
        serverDestroyer = Executors.newSingleThreadExecutor();
        serverDestroyer.execute(() -> {
            try {
                server.awaitTermination();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

    }

    @PreDestroy
    public void shutdown() {
        serverDestroyer.shutdown();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(ShoppingCartServer.class, args);
    }

}
