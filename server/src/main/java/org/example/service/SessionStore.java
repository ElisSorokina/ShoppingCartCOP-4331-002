package org.example.service;

import org.example.grpc.Role;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionStore {
    private final Map<UUID, Role> sessionStore = new ConcurrentHashMap<>();

    public void registerSession(UUID sessionId, Role role) {
        sessionStore.put(sessionId, role);
    }

    public Role getRole(UUID sessionId) {
        return sessionStore.get(sessionId);

    }
}
