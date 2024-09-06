package org.example;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.LoginRequest;
import org.example.grpc.LoginResponse;
import org.example.grpc.LoginServiceGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
public class LoginController {

    @Value("${server.host}")
    private String serverHost;

    @Value("${server.port}")
    private int serverPort;

    private LoginServiceGrpc.LoginServiceBlockingStub loginServiceStub;


    @PostConstruct
    public void init() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverHost, serverPort)
                .usePlaintext()
                .build();
        loginServiceStub = LoginServiceGrpc.newBlockingStub(channel);
    }

    public String login(String userName, String password) {
        LoginResponse loginResponse = loginServiceStub.login(LoginRequest.newBuilder()
                .setLogin(userName)
                .setPassword(password)
                .build());

        String sessionId = loginResponse.getSessionId();
        System.out.println(sessionId);
        return sessionId;
    }
}
