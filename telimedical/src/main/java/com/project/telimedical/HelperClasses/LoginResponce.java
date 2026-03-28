package com.project.telimedical.HelperClasses;

import com.project.telimedical.entities.Role;

public class LoginResponce {

    private String token;
    private Role role;
    private String username;
    private String id;
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    


}



