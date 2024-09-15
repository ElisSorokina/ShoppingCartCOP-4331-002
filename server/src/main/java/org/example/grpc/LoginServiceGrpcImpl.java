package org.example.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.example.exceptions.FailedAuthenticationException;
import org.example.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceGrpcImpl extends LoginServiceGrpc.LoginServiceImplBase {

    @Autowired
    private LoginService loginService;

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        System.out.println("Request received from client:\n" + request);
        try {
            LoginResponse response = loginService.login(request);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            System.out.println("Successfully authenticated:\n" + request.getLogin());
        } catch (FailedAuthenticationException e) {
            // Invalid credentials
            System.err.println("Failed to authenticate:\n" + request.getLogin());
            responseObserver.onError(Status.UNAUTHENTICATED.withDescription("Invalid credentials").asRuntimeException());
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(Status.INTERNAL.withDescription("Unknown server error").asRuntimeException());
        }
    }
}