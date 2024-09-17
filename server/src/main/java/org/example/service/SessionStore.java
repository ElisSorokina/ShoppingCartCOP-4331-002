package org.example.service;

import org.example.data.model.User;
import org.example.grpc.Role;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionStore {
    private final Map<UUID, User> sessionStore = new ConcurrentHashMap<>();

    public void registerSession(UUID sessionId, User user) {
        sessionStore.put(sessionId, user);
    }

    public Role getRole(UUID sessionId) {
        User user = sessionStore.get(sessionId);
        return user.getRole();

    }
    public User getUser(UUID sessionId){
        return sessionStore.get(sessionId);

    }
}
