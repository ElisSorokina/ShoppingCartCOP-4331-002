package org.example;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.*;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
public class MainController {

    @Value("${server.host}")
    private String serverHost;

    @Value("${server.port}")
    private int serverPort;

    private LoginServiceGrpc.LoginServiceBlockingStub loginServiceStub;
    private SignUpServiceGrpc.SignUpServiceBlockingStub signUpServiceStub;
    private LoginDialog loginDialog;
    private SellerWindow sellerWindow;
    private String sessionId;

    public void init() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverHost, serverPort)
                .usePlaintext()
                .build();
        loginServiceStub = LoginServiceGrpc.newBlockingStub(channel);
        signUpServiceStub = SignUpServiceGrpc.newBlockingStub(channel);
        loginDialog = new LoginDialog(this);
        loginDialog.setVisible(true);
        sellerWindow = new SellerWindow();

    }

    public void login(String userName, String password) {
        LoginResponse loginResponse = loginServiceStub.login(LoginRequest.newBuilder()
                .setLogin(userName)
                .setPassword(password)
                .build());

        sessionId = loginResponse.getSessionId();
        System.out.println(sessionId);
        sellerWindow.setVisible(true);

    }

    public void signUp() {
        RegistrationForm form = new RegistrationForm(this);
        form.setVisible(true);
    }

    public void completeSignUp(SignUpRequest signUpRequest) {
        signUpServiceStub.signUp(signUpRequest);
    }
}
