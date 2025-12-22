package com.ticketvoyage.service;

import com.ticketvoyage.model.*;
import java.util.*;

public class DataService {
    private Map<String, User> users = new HashMap<>();
    private Map<String, Route> routes = new HashMap<>();
    private Map<String, Ticket> tickets = new HashMap<>();

    public DataService() {
        initSampleData();
    }
    
    private void initSampleData() {
        tickets.clear();

        if (!users.containsKey("auto")) {
            User autoUser = new User("auto", "auto_user", "auto123", "Автоматический пользователь", "ADMIN");
            users.put("auto", autoUser);
        }

        if (routes.isEmpty()) {
            Route route1 = new Route();
            route1.setId("1");
            route1.setFrom("Москва");
            route1.setTo("Санкт-Петербург");
            route1.setStops("Тверь");
            route1.setDepartureTime("08:30");
            route1.setArrivalTime("12:45");
            route1.setDate("2025-10-27");
            route1.setPrice(1500.0);
            route1.setTotalSeats(40);
            routes.put("1", route1);

            Route route2 = new Route();
            route2.setId("2");
            route2.setFrom("Москва");
            route2.setTo("Казань");
            route2.setStops("Нижний Новгород");
            route2.setDepartureTime("21:00");
            route2.setArrivalTime("08:30");
            route2.setDate("2025-10-28");
            route2.setPrice(2200.0);
            route2.setTotalSeats(40);
            routes.put("2", route2);
        }
    }
    
    public User findUserByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    public List<Route> getAllRoutes() {
        List<Route> result = new ArrayList<>();
        for (Route route : routes.values()) {
            Route routeCopy = new Route();
            routeCopy.setId(route.getId());
            routeCopy.setFrom(route.getFrom());
            routeCopy.setTo(route.getTo());
            routeCopy.setStops(route.getStops());
            routeCopy.setDepartureTime(route.getDepartureTime());
            routeCopy.setArrivalTime(route.getArrivalTime());
            routeCopy.setDate(route.getDate());
            routeCopy.setPrice(route.getPrice());
            routeCopy.setTotalSeats(route.getTotalSeats());
            routeCopy.setTickets(getTicketsForRoute(route.getId()));
            result.add(routeCopy);
        }
        return result;
    }

    public Route findRouteById(String id) {
        Route route = routes.get(id);
        if (route != null) {
            Route routeCopy = new Route();
            routeCopy.setId(route.getId());
            routeCopy.setFrom(route.getFrom());
            routeCopy.setTo(route.getTo());
            routeCopy.setStops(route.getStops());
            routeCopy.setDepartureTime(route.getDepartureTime());
            routeCopy.setArrivalTime(route.getArrivalTime());
            routeCopy.setDate(route.getDate());
            routeCopy.setPrice(route.getPrice());
            routeCopy.setTotalSeats(route.getTotalSeats());
            routeCopy.setTickets(getTicketsForRoute(route.getId()));
            return routeCopy;
        }
        return null;
    }

    public void addRoute(Route route) {
        routes.put(route.getId(), route);
    }
    
    public void addTicket(Ticket ticket) {
        tickets.put(ticket.getId(), ticket);
    }

    public List<Ticket> getUserTickets(String userId) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket ticket : tickets.values()) {
            if (userId.equals(ticket.getUserId())) {
                result.add(ticket);
            }
        }
        return result;
    }

    public List<Ticket> getAllTickets() {
        return new ArrayList<>(tickets.values());
    }

    public void cancelTicket(String ticketId) {
        Ticket ticket = tickets.get(ticketId);
        if (ticket != null) {
            ticket.setStatus("CANCELLED");
        }
    }

    public boolean isSeatAvailable(String routeId, String seatNumber) {
        for (Ticket ticket : tickets.values()) {
            if (routeId.equals(ticket.getRouteId()) &&
                seatNumber.equals(ticket.getSeatNumber()) &&
                "BOOKED".equals(ticket.getStatus())) {
                return false;
            }
        }
        return true;
    }
    
    private List<Ticket> getTicketsForRoute(String routeId) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket ticket : tickets.values()) {
            if (routeId.equals(ticket.getRouteId())) {
                result.add(ticket);
            }
        }
        return result;
    }
}