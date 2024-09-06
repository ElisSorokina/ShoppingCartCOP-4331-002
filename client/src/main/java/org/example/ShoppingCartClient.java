package org.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.LoginRequest;
import org.example.grpc.LoginResponse;
import org.example.grpc.LoginServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static org.example.grpc.LoginServiceGrpc.*;

@SpringBootApplication
public class ShoppingCartClient extends JFrame {

    @Autowired
    private LoginController controller;

    @PostConstruct
    public void initUI() {
        var loginDialog = new LoginDialog(this, controller);
        loginDialog.setVisible(true);
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