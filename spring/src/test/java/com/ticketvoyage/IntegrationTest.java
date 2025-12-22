package com.ticketvoyage;

import com.ticketvoyage.service.DataService;
import com.ticketvoyage.service.AuthService;
import com.ticketvoyage.model.User;
import com.ticketvoyage.model.Route;
import com.ticketvoyage.model.Ticket;
import java.util.List;
import java.util.UUID;

public class IntegrationTest {
    
    private DataService dataService;
    private AuthService authService;
    
    public void setUp() {
        dataService = new DataService();
        authService = new AuthService(dataService);
    }
    
    public void testFullBookingWorkflow() {
        User user = dataService.findUserByEmail("auto_user");
        authService.login("auto_user", "auto123");
        
        assert authService.getCurrentUser() != null : "User should be logged in";
        assert "ADMIN".equals(authService.getCurrentUser().getRole()) : "User should be admin";
        
        List<Route> routes = dataService.getAllRoutes();
        assert routes.size() > 0 : "Should have routes available";
        
        Route selectedRoute = routes.get(0);
        assert dataService.isSeatAvailable(selectedRoute.getId(), "5A") : "Seat should be available";
        
        Ticket ticket = new Ticket(UUID.randomUUID().toString(), user.getId(), selectedRoute.getId(), "5A",
                                  "Иван Иванов", "ivan@example.com", "+79999999999", "BOOKED");
        dataService.addTicket(ticket);
        
        assert !dataService.isSeatAvailable(selectedRoute.getId(), "5A") : "Seat should not be available after booking";
        
        dataService.cancelTicket(ticket.getId());
        assert dataService.isSeatAvailable(selectedRoute.getId(), "5A") : "Seat should be available after cancellation";
        
        System.out.println("✓ testFullBookingWorkflow passed");
    }
    
    public void runAllTests() {
        setUp();
        
        try {
            testFullBookingWorkflow();
            System.out.println("All integration tests passed!");
        } catch (AssertionError e) {
            System.err.println("Integration test failed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        IntegrationTest test = new IntegrationTest();
        test.runAllTests();
    }
}
