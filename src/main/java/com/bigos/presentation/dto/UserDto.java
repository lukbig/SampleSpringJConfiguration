package com.bigos.presentation.dto;

import com.bigos.domain.users.Privileges;

public class UserDto {
    private String username;
    private String password;
    private Privileges privileges;

    public UserDto() {
    }

    public UserDto(String username, String password, Privileges privileges) {
        this.username = username;
        this.password = password;
        this.privileges = privileges;
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
