package org.example.service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import org.example.data.model.User;
import org.example.data.repository.UserRepository;
import org.example.exceptions.FailedAuthenticationException;
import org.example.grpc.LoginRequest;
import org.example.grpc.LoginResponse;
import org.example.grpc.LoginServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class LoginService extends LoginServiceGrpc.LoginServiceImplBase {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionStore sessionStore;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public LoginResponse login(LoginRequest request) {
         var user = userRepository.findByLogin(request.getLogin());
        // Validate user credentials
        var encodedPassword = user.get().getPassword();
        var rawPassword = request.getPassword();
        if (user.isPresent() && passwordEncoder.matches(rawPassword, encodedPassword)) {
            // Generate a new session token
            var sessionId = UUID.randomUUID();
            sessionStore.registerSession(sessionId, request.getRole());

            // Send the session token back to the client
            return LoginResponse.newBuilder().setSessionId(sessionId.toString()).build();
        } else {
           throw new FailedAuthenticationException();
        }

    }
}
