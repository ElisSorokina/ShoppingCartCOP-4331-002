package org.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.LoginRequest;
import org.example.grpc.LoginResponse;
import org.example.grpc.LoginServiceGrpc;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static org.example.grpc.LoginServiceGrpc.*;

@SpringBootApplication
public class ShoppingCartClient extends JFrame {

    public ShoppingCartClient() {
        initUI();
    }

    private void initUI() {

        var loginButton = new JButton("Login");

        loginButton.addActionListener((ActionEvent event) -> {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081)
                    .usePlaintext()
                    .build();

            LoginServiceBlockingStub stub
                    = LoginServiceGrpc.newBlockingStub(channel);

            LoginResponse loginResponse = stub.login(LoginRequest.newBuilder()
                    .setLogin("test")
                    .setPassword("bad")
                    .build());

            System.out.println("Login response: " + loginResponse.getResult());
        });

        createLayout(loginButton);

        setTitle("Quit button");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void createLayout(JComponent... arg) {

        var pane = getContentPane();
        var gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
        );
    }

    public static void main(String[] args) {

        var ctx = new SpringApplicationBuilder(ShoppingCartClient.class)
                .headless(false).web(WebApplicationType.NONE).run(args);

        EventQueue.invokeLater(() -> {

            var ex = ctx.getBean(ShoppingCartClient.class);
            ex.setVisible(true);
        });
    }
}