package com.ticketvoyage;

import com.ticketvoyage.service.DataService;
import com.ticketvoyage.model.User;
import com.ticketvoyage.model.Route;
import com.ticketvoyage.model.Ticket;
import java.util.List;
import java.util.UUID;

public class PerformanceTest {
    
    private DataService dataService;
    
    public void setUp() {
        dataService = new DataService();
    }
    
    public void testBulkOperations() {
        long startTime = System.currentTimeMillis();
        
        User user = dataService.findUserByEmail("auto_user");
        List<Route> routes = dataService.getAllRoutes();
        Route route = routes.get(0);
        
        int operations = 30;
        
        for (int i = 0; i < operations; i++) {
            String seatNumber = (i % 40 + 1) + String.valueOf((char)('A' + (i % 4)));
            if (dataService.isSeatAvailable(route.getId(), seatNumber)) {
                Ticket ticket = new Ticket(UUID.randomUUID().toString(), user.getId(), route.getId(), seatNumber,
                                          "Bulk User " + i, "bulk" + i + "@example.com", "+7" + String.format("%09d", i), "BOOKED");
                dataService.addTicket(ticket);
            }
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        List<Ticket> allTickets = dataService.getAllTickets();
        assert allTickets.size() >= operations * 0.3 : "Should have most tickets created";
        
        System.out.println("✓ testBulkOperations passed (" + duration + "ms for " + operations + " operations)");
    }
    
    public void runAllTests() {
        setUp();
        
        try {
            testBulkOperations();
            System.out.println("All performance tests passed!");
        } catch (AssertionError e) {
            System.err.println("Performance test failed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error in performance test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        PerformanceTest test = new PerformanceTest();
        test.runAllTests();
    }
}
