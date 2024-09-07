package org.example.service;

import io.grpc.stub.StreamObserver;
import org.example.grpc.LoginRequest;
import org.example.grpc.LoginResponse;
import org.example.grpc.LoginServiceGrpc;
import org.springframework.stereotype.Controller;

@Controller
public class LoginServiceImpl extends LoginServiceGrpc.LoginServiceImplBase {

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        System.out.println("Request received from client:\n" + request);

        LoginResponse response = LoginResponse.newBuilder()
                .setSessionId("sessionId")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
