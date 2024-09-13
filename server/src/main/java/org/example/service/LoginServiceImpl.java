package org.example.service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.example.data.model.User;
import org.example.data.repository.UserRepository;
import org.example.grpc.LoginRequest;
import org.example.grpc.LoginResponse;
import org.example.grpc.LoginServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class LoginServiceImpl extends LoginServiceGrpc.LoginServiceImplBase {
    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionStore sessionStore;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        System.out.println("Request received from client:\n" + request);

        var user = userRepository.findByLogin(request.getLogin());
        // Validate user credentials
        var encodedPassword = user.get().getPassword();
        var rawPassword = request.getPassword();
        if (user.isPresent() && passwordEncoder.matches(rawPassword, encodedPassword)) {
            // Generate a new session token
            var sessionId = UUID.randomUUID();
            sessionStore.registerSession(sessionId, request.getRole());

            // Send the session token back to the client
            LoginResponse response = LoginResponse.newBuilder().setSessionId(sessionId.toString()).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            System.out.println("Successfully authenticated:\n" + request.getLogin());
        } else {
            System.err.println("Failed to authenticate:\n" + request.getLogin());
            // Invalid credentials
            responseObserver.onError(Status.UNAUTHENTICATED.withDescription("Invalid credentials").asRuntimeException());
        }

    }
}
