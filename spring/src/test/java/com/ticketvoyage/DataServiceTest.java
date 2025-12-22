package com.ticketvoyage;

import com.ticketvoyage.service.DataService;
import com.ticketvoyage.model.User;
import com.ticketvoyage.model.Route;
import com.ticketvoyage.model.Ticket;
import java.util.List;
import java.util.UUID;

public class DataServiceTest {
    
    private DataService dataService;
    
    public void setUp() {
        dataService = new DataService();
    }
    
    public void testFindUserByEmail() {
        User user = dataService.findUserByEmail("auto_user");
        assert user != null : "User should be found";
        assert "auto_user".equals(user.getEmail()) : "Email should match";
    }
    
    public void testGetAllRoutes() {
        List<Route> routes = dataService.getAllRoutes();
        assert routes.size() == 2 : "Should have 2 routes initially";
    }
    
    public void runAllTests() {
        setUp();
        
        try {
            testFindUserByEmail();
            System.out.println("✓ testFindUserByEmail passed");
            
            testGetAllRoutes();
            System.out.println("✓ testGetAllRoutes passed");
            
            System.out.println("All DataService tests passed!");
        } catch (AssertionError e) {
            System.err.println("Test failed: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        DataServiceTest test = new DataServiceTest();
        test.runAllTests();
    }
}
