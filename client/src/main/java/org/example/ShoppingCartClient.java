package org.example;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.awt.*;

@SpringBootApplication
public class ShoppingCartClient {

    public static void main(String[] args) {
        var ctx = new SpringApplicationBuilder(ShoppingCartClient.class)
                .headless(false).web(WebApplicationType.NONE).run(args);

        EventQueue.invokeLater(() -> {
            var controller = ctx.getBean(MainController.class);
            controller.init();
        });
    }
}