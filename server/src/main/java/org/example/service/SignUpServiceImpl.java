package org.example.service;

import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import org.example.data.model.User;
import org.example.data.repository.UserRepository;
import org.example.grpc.SignUpRequest;
import org.example.grpc.SignUpResponse;
import org.example.grpc.SignUpServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

@Controller
public class SignUpServiceImpl extends SignUpServiceGrpc.SignUpServiceImplBase {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public void signUp(SignUpRequest request, StreamObserver<SignUpResponse> responseObserver) {
        System.out.println("Request received from client:\n" + request);

        SignUpResponse.Builder responseBuilder = SignUpResponse.newBuilder();
        try {
            User user = new User();
            user.setLogin(request.getLogin());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());
            userRepository.save(user);
            System.out.println("User saved successfully.");

        } catch (Exception e) {
            responseBuilder.setErrorMsg(e.getMessage());
                e.printStackTrace();  // Print the full stack trace for debugging
                responseBuilder.setErrorMsg(e.getMessage());
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
