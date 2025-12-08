package com.ticketvoyage.service;

import com.ticketvoyage.model.User;
import java.util.UUID;

public class AuthService {
    private DataService dataService;
    private User currentUser;
    
    public AuthService(DataService dataService) {
        this.dataService = dataService;
    }
    
    public User login(String email, String password) {
        User user = dataService.findUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return user;
        }
        return null;
    }
    
    public User register(String email, String password, String name) {
        if (dataService.findUserByEmail(email) != null) {
            return null;
        }
        
        User newUser = new User(
            UUID.randomUUID().toString(),
            email,
            password,
            name,
            "USER"
        );
        
        dataService.addUser(newUser);
        currentUser = newUser;
        return newUser;
    }
    
    public void logout() {
        currentUser = null;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean isAdmin() {
        return currentUser != null && "ADMIN".equals(currentUser.getRole());
    }
}