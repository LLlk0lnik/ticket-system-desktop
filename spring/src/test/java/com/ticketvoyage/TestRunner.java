package com.ticketvoyage;

public class TestRunner {
    
    public static void main(String[] args) {
        System.out.println("Starting TicketVoyage Test Suite");
        System.out.println("=================================");
        
        try {
            System.out.println("\n--- Running Unit Tests ---");
            DataServiceTest.main(new String[0]);
            
            System.out.println("\n--- Running Integration Tests ---");
            IntegrationTest.main(new String[0]);
            
            System.out.println("\n--- Running Functional Tests ---");
            FunctionalTest.main(new String[0]);
            
            System.out.println("\n--- Running Performance Tests ---");
            PerformanceTest.main(new String[0]);
            
            System.out.println("\n=================================");
            System.out.println("All tests completed successfully!");
            System.out.println("=================================");
            
        } catch (Exception e) {
            System.err.println("Test suite failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
