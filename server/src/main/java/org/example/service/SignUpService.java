package org.example.service;

import jakarta.transaction.Transactional;
import org.example.data.model.Cart;
import org.example.data.model.User;
import org.example.data.repository.CartRepository;
import org.example.data.repository.UserRepository;
import org.example.grpc.Role;
import org.example.grpc.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public void signUp(SignUpRequest request) {
        System.out.println("Request received from client:\n" + request);
        User user = new User();
        user.setLogin(request.getLogin());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        User savedUser = userRepository.save(user);
        if(request.getRole()== Role.BUYER){
            Cart cart = new Cart();
            cart.setBuyer(savedUser);
            cartRepository.save(cart);
        }
    }

}
