package org.example.data.model;

import jakarta.persistence.*;
import org.example.grpc.Role;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a user in the system.
 */
@Entity
@Table(
        name = "user",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"login"})}
)
public class User {
    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * The login name of the user.
     */
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    /**
     * The password of the user.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * The role of the user (e.g., buyer or seller).
     */
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
