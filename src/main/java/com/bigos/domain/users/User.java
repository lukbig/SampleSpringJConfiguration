package com.bigos.domain.users;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity(name = "users")
public class User {

    @Id
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Privileges privileges;

    public User(String username, String password, Privileges privileges) {
        this.username = username;
        this.password = password;
        this.privileges = privileges;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Privileges getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Privileges privileges) {
        this.privileges = privileges;
    }
}
