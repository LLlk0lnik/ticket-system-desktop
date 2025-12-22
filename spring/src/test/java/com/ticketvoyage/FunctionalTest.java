package com.ticketvoyage;

import com.ticketvoyage.service.DataService;
import com.ticketvoyage.service.AuthService;
import com.ticketvoyage.model.User;
import com.ticketvoyage.model.Route;
import com.ticketvoyage.model.Ticket;
import java.util.List;
import java.util.UUID;

public class FunctionalTest {
    
    private DataService dataService;
    private AuthService authService;
    
    public void setUp() {
        dataService = new DataService();
        authService = new AuthService(dataService);
    }
    
    public void testUserRegistrationAndLogin() {
        User newUser = new User(UUID.randomUUID().toString(), "testuser@example.com", "testpass123", "Test User", "USER");
        dataService.addUser(newUser);
        
        User foundUser = dataService.findUserByEmail("testuser@example.com");
        assert foundUser != null : "New user should be found";
        assert "Test User".equals(foundUser.getName()) : "User name should match";
        
        User loggedInUser = authService.login("testuser@example.com", "testpass123");
        assert loggedInUser != null : "Login should succeed";
        assert !authService.isAdmin() : "Regular user should not be admin";
        
        System.out.println("✓ testUserRegistrationAndLogin passed");
    }
    
    public void runAllTests() {
        setUp();
        
        try {
            testUserRegistrationAndLogin();
            System.out.println("All functional tests passed!");
        } catch (AssertionError e) {
            System.err.println("Functional test failed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error in functional test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        FunctionalTest test = new FunctionalTest();
        test.runAllTests();
    }
}
