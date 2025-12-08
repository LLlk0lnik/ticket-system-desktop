package com.ticketvoyage.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Route implements Serializable {
    private String id;
    private String from;
    private String to;
    private String stops;
    private String departureTime;
    private String arrivalTime;
    private String date;
    private double price;
    private int totalSeats;
    private List<Ticket> tickets;
    
    public Route() {
        this.tickets = new ArrayList<>();
    }
    
    public Route(String id, String from, String to, String stops, String departureTime, 
                 String arrivalTime, String date, double price, int totalSeats) {
        this();
        this.id = id;
        this.from = from;
        this.to = to;
        this.stops = stops;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.date = date;
        this.price = price;
        this.totalSeats = totalSeats;
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }
    
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    
    public String getStops() { return stops; }
    public void setStops(String stops) { this.stops = stops; }
    
    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    
    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    
    public List<Ticket> getTickets() { return tickets; }
    public void setTickets(List<Ticket> tickets) { this.tickets = tickets; }
    
    public int getBookedSeats() {
        return (int) tickets.stream().filter(t -> "BOOKED".equals(t.getStatus())).count();
    }
}