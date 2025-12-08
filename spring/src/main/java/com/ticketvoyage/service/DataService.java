package com.ticketvoyage.service;

import com.ticketvoyage.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataService {
    private static final String DB_URL = "jdbc:sqlite:ticketvoyage.db";
    private Connection connection;
    
    public DataService() {
        try {
            connect();
            initSchema();
            initSampleData();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка инициализации базы данных", e);
        }
    }
    
    private void connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("Не найден драйвер SQLite. Поместите sqlite-jdbc.jar в папку spring/lib и запустите с ним в classpath.");
        }
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
    }
    
    private void initSchema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS users (" +
                "id TEXT PRIMARY KEY," +
                "email TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "name TEXT," +
                "role TEXT NOT NULL)"
            );
            
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS routes (" +
                "id TEXT PRIMARY KEY," +
                "from_city TEXT NOT NULL," +
                "to_city TEXT NOT NULL," +
                "stops TEXT," +
                "departure_time TEXT NOT NULL," +
                "arrival_time TEXT NOT NULL," +
                "date TEXT NOT NULL," +
                "price REAL NOT NULL," +
                "total_seats INTEGER NOT NULL)"
            );
            
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS tickets (" +
                "id TEXT PRIMARY KEY," +
                "user_id TEXT," +
                "route_id TEXT NOT NULL," +
                "seat_number TEXT NOT NULL," +
                "passenger_name TEXT," +
                "passenger_email TEXT," +
                "passenger_phone TEXT," +
                "status TEXT NOT NULL," +
                "created_at TEXT NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES users(id)," +
                "FOREIGN KEY (route_id) REFERENCES routes(id))"
            );

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS schedule (" +
                "number TEXT," +
                "from_city TEXT NOT NULL," +
                "to_city TEXT NOT NULL," +
                "departure_time TEXT NOT NULL," +
                "arrival_time TEXT NOT NULL," +
                "duration TEXT NOT NULL," +
                "days TEXT NOT NULL," +
                "price TEXT NOT NULL," +
                "seats INTEGER NOT NULL)"
            );
        }
    }
    
    private void initSampleData() throws SQLException {
        boolean hasUsers;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users")) {
            hasUsers = rs.next() && rs.getInt(1) > 0;
        }
        
        if (!hasUsers) {
            try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO users (id, email, password, name, role) VALUES (?, ?, ?, ?, ?)")) {
                ps.setString(1, "1");
                ps.setString(2, "admin");
                ps.setString(3, "admin123");
                ps.setString(4, "Admin");
                ps.setString(5, "ADMIN");
                ps.addBatch();
                
                ps.setString(1, "2");
                ps.setString(2, "user");
                ps.setString(3, "user123");
                ps.setString(4, "User");
                ps.setString(5, "USER");
                ps.addBatch();
                
                ps.executeBatch();
            }
        }
        
        boolean hasRoutes;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM routes")) {
            hasRoutes = rs.next() && rs.getInt(1) > 0;
        }
        
        if (!hasRoutes) {
            try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO routes (id, from_city, to_city, stops, departure_time, arrival_time, date, price, total_seats) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                ps.setString(1, "1");
                ps.setString(2, "Москва");
                ps.setString(3, "Санкт-Петербург");
                ps.setString(4, "Тверь");
                ps.setString(5, "08:30");
                ps.setString(6, "12:45");
                ps.setString(7, "2025-10-27");
                ps.setDouble(8, 1500.0);
                ps.setInt(9, 40);
                ps.addBatch();
                
                ps.setString(1, "2");
                ps.setString(2, "Москва");
                ps.setString(3, "Казань");
                ps.setString(4, "Нижний Новгород");
                ps.setString(5, "21:00");
                ps.setString(6, "08:30");
                ps.setString(7, "2025-10-28");
                ps.setDouble(8, 2200.0);
                ps.setInt(9, 40);
                ps.addBatch();
                
                ps.executeBatch();
            }
        }

        boolean hasSchedule;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM schedule")) {
            hasSchedule = rs.next() && rs.getInt(1) > 0;
        }

        if (!hasSchedule) {
            try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO schedule (number, from_city, to_city, departure_time, arrival_time, duration, days, price, seats) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                ps.setString(1, "101");
                ps.setString(2, "Москва");
                ps.setString(3, "Санкт-Петербург");
                ps.setString(4, "06:30");
                ps.setString(5, "10:45");
                ps.setString(6, "4ч 15м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "1200₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "102");
                ps.setString(2, "Москва");
                ps.setString(3, "Санкт-Петербург");
                ps.setString(4, "08:30");
                ps.setString(5, "12:45");
                ps.setString(6, "4ч 15м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "1500₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "103");
                ps.setString(2, "Москва");
                ps.setString(3, "Санкт-Петербург");
                ps.setString(4, "12:00");
                ps.setString(5, "16:15");
                ps.setString(6, "4ч 15м");
                ps.setString(7, "Пн-Пт");
                ps.setString(8, "1400₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "104");
                ps.setString(2, "Москва");
                ps.setString(3, "Санкт-Петербург");
                ps.setString(4, "15:00");
                ps.setString(5, "19:30");
                ps.setString(6, "4ч 30м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "1800₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "105");
                ps.setString(2, "Москва");
                ps.setString(3, "Санкт-Петербург");
                ps.setString(4, "18:45");
                ps.setString(5, "23:00");
                ps.setString(6, "4ч 15м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "1600₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "106");
                ps.setString(2, "Москва");
                ps.setString(3, "Санкт-Петербург");
                ps.setString(4, "22:00");
                ps.setString(5, "02:15");
                ps.setString(6, "4ч 15м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "1200₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "201");
                ps.setString(2, "Санкт-Петербург");
                ps.setString(3, "Москва");
                ps.setString(4, "07:00");
                ps.setString(5, "11:15");
                ps.setString(6, "4ч 15м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "1200₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "202");
                ps.setString(2, "Санкт-Петербург");
                ps.setString(3, "Москва");
                ps.setString(4, "10:30");
                ps.setString(5, "14:45");
                ps.setString(6, "4ч 15м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "1500₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "203");
                ps.setString(2, "Санкт-Петербург");
                ps.setString(3, "Москва");
                ps.setString(4, "14:00");
                ps.setString(5, "18:15");
                ps.setString(6, "4ч 15м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "1600₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "204");
                ps.setString(2, "Санкт-Петербург");
                ps.setString(3, "Москва");
                ps.setString(4, "17:30");
                ps.setString(5, "21:45");
                ps.setString(6, "4ч 15м");
                ps.setString(7, "Пн-Сб");
                ps.setString(8, "1700₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "205");
                ps.setString(2, "Санкт-Петербург");
                ps.setString(3, "Москва");
                ps.setString(4, "20:00");
                ps.setString(5, "00:15");
                ps.setString(6, "4ч 15м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "1400₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "301");
                ps.setString(2, "Москва");
                ps.setString(3, "Казань");
                ps.setString(4, "08:00");
                ps.setString(5, "19:30");
                ps.setString(6, "11ч 30м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "2500₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "302");
                ps.setString(2, "Москва");
                ps.setString(3, "Казань");
                ps.setString(4, "14:30");
                ps.setString(5, "02:00");
                ps.setString(6, "11ч 30м");
                ps.setString(7, "Чт-Вс");
                ps.setString(8, "2300₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "303");
                ps.setString(2, "Москва");
                ps.setString(3, "Казань");
                ps.setString(4, "21:00");
                ps.setString(5, "08:30");
                ps.setString(6, "11ч 30м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "2200₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "401");
                ps.setString(2, "Казань");
                ps.setString(3, "Москва");
                ps.setString(4, "09:00");
                ps.setString(5, "20:30");
                ps.setString(6, "11ч 30м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "2500₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "402");
                ps.setString(2, "Казань");
                ps.setString(3, "Москва");
                ps.setString(4, "16:00");
                ps.setString(5, "03:30");
                ps.setString(6, "11ч 30м");
                ps.setString(7, "Пн-Пт");
                ps.setString(8, "2400₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "403");
                ps.setString(2, "Казань");
                ps.setString(3, "Москва");
                ps.setString(4, "22:30");
                ps.setString(5, "10:00");
                ps.setString(6, "11ч 30м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "2200₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "501");
                ps.setString(2, "Москва");
                ps.setString(3, "Новгород");
                ps.setString(4, "07:30");
                ps.setString(5, "10:00");
                ps.setString(6, "2ч 30м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "800₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "502");
                ps.setString(2, "Москва");
                ps.setString(3, "Новгород");
                ps.setString(4, "11:00");
                ps.setString(5, "13:30");
                ps.setString(6, "2ч 30м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "900₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "503");
                ps.setString(2, "Москва");
                ps.setString(3, "Новгород");
                ps.setString(4, "15:00");
                ps.setString(5, "17:30");
                ps.setString(6, "2ч 30м");
                ps.setString(7, "Пн-Пт");
                ps.setString(8, "850₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "504");
                ps.setString(2, "Москва");
                ps.setString(3, "Новгород");
                ps.setString(4, "19:00");
                ps.setString(5, "21:30");
                ps.setString(6, "2ч 30м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "950₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "601");
                ps.setString(2, "Новгород");
                ps.setString(3, "Москва");
                ps.setString(4, "08:00");
                ps.setString(5, "10:30");
                ps.setString(6, "2ч 30м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "800₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "602");
                ps.setString(2, "Новгород");
                ps.setString(3, "Москва");
                ps.setString(4, "12:30");
                ps.setString(5, "15:00");
                ps.setString(6, "2ч 30м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "900₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "603");
                ps.setString(2, "Новгород");
                ps.setString(3, "Москва");
                ps.setString(4, "16:30");
                ps.setString(5, "19:00");
                ps.setString(6, "2ч 30м");
                ps.setString(7, "Пн-Сб");
                ps.setString(8, "850₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "604");
                ps.setString(2, "Новгород");
                ps.setString(3, "Москва");
                ps.setString(4, "20:00");
                ps.setString(5, "22:30");
                ps.setString(6, "2ч 30м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "950₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "701");
                ps.setString(2, "Санкт-Петербург");
                ps.setString(3, "Новгород");
                ps.setString(4, "09:30");
                ps.setString(5, "11:00");
                ps.setString(6, "1ч 30м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "600₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "702");
                ps.setString(2, "Санкт-Петербург");
                ps.setString(3, "Новгород");
                ps.setString(4, "14:00");
                ps.setString(5, "15:30");
                ps.setString(6, "1ч 30м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "650₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "703");
                ps.setString(2, "Санкт-Петербург");
                ps.setString(3, "Новгород");
                ps.setString(4, "18:30");
                ps.setString(5, "20:00");
                ps.setString(6, "1ч 30м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "700₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "801");
                ps.setString(2, "Новгород");
                ps.setString(3, "Санкт-Петербург");
                ps.setString(4, "10:00");
                ps.setString(5, "11:30");
                ps.setString(6, "1ч 30м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "600₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "802");
                ps.setString(2, "Новгород");
                ps.setString(3, "Санкт-Петербург");
                ps.setString(4, "13:00");
                ps.setString(5, "14:30");
                ps.setString(6, "1ч 30м");
                ps.setString(7, "Пн-Пт");
                ps.setString(8, "650₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.setString(1, "803");
                ps.setString(2, "Новгород");
                ps.setString(3, "Санкт-Петербург");
                ps.setString(4, "17:00");
                ps.setString(5, "18:30");
                ps.setString(6, "1ч 30м");
                ps.setString(7, "Ежедневно");
                ps.setString(8, "700₽");
                ps.setInt(9, 40);
                ps.addBatch();

                ps.executeBatch();
            }
        }
    }
    
    public User findUserByEmail(String email) {
        String sql = "SELECT id, email, password, name, role FROM users WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getString("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка поиска пользователя: " + e.getMessage());
        }
        return null;
    }
    
    public void addUser(User user) {
        String sql = "INSERT INTO users (id, email, password, name, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getName());
            ps.setString(5, user.getRole());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка добавления пользователя: " + e.getMessage());
        }
    }
    
    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        String sql = "SELECT id, email, password, name, role FROM users";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.add(new User(
                    rs.getString("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка получения пользователей: " + e.getMessage());
        }
        return result;
    }
    
    public List<Route> getAllRoutes() {
        List<Route> result = new ArrayList<>();
        String sql = "SELECT * FROM routes";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Route route = mapRoute(rs);
                route.setTickets(getTicketsForRoute(route.getId()));
                result.add(route);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка получения маршрутов: " + e.getMessage());
        }
        return result;
    }
    
    public Route findRouteById(String id) {
        String sql = "SELECT * FROM routes WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Route route = mapRoute(rs);
                    route.setTickets(getTicketsForRoute(route.getId()));
                    return route;
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка поиска маршрута: " + e.getMessage());
        }
        return null;
    }
    
    public void addRoute(Route route) {
        String sql = "INSERT INTO routes (id, from_city, to_city, stops, departure_time, arrival_time, date, price, total_seats) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, route.getId());
            ps.setString(2, route.getFrom());
            ps.setString(3, route.getTo());
            ps.setString(4, route.getStops());
            ps.setString(5, route.getDepartureTime());
            ps.setString(6, route.getArrivalTime());
            ps.setString(7, route.getDate());
            ps.setDouble(8, route.getPrice());
            ps.setInt(9, route.getTotalSeats());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка добавления маршрута: " + e.getMessage());
        }
    }
    
    public void addTicket(Ticket ticket) {
        String sql = "INSERT INTO tickets (id, user_id, route_id, seat_number, passenger_name, passenger_email, passenger_phone, status, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, ticket.getId());
            ps.setString(2, ticket.getUserId());
            ps.setString(3, ticket.getRouteId());
            ps.setString(4, ticket.getSeatNumber());
            ps.setString(5, ticket.getPassengerName());
            ps.setString(6, ticket.getPassengerEmail());
            ps.setString(7, ticket.getPassengerPhone());
            ps.setString(8, ticket.getStatus());
            ps.setString(9, ticket.getCreatedAt());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка добавления билета: " + e.getMessage());
        }
    }
    
    public List<Ticket> getUserTickets(String userId) {
        List<Ticket> result = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapTicket(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка получения билетов пользователя: " + e.getMessage());
        }
        return result;
    }
    
    public List<Ticket> getAllTickets() {
        List<Ticket> result = new ArrayList<>();
        String sql = "SELECT * FROM tickets";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.add(mapTicket(rs));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка получения билетов: " + e.getMessage());
        }
        return result;
    }
    
    public void cancelTicket(String ticketId) {
        String sql = "UPDATE tickets SET status = 'CANCELLED' WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, ticketId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка отмены билета: " + e.getMessage());
        }
    }
    
    public boolean isSeatAvailable(String routeId, String seatNumber) {
        String sql = "SELECT COUNT(*) FROM tickets WHERE route_id = ? AND seat_number = ? AND status = 'BOOKED'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, routeId);
            ps.setString(2, seatNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка проверки места: " + e.getMessage());
        }
        return false;
    }

    public Object[][] getScheduleData() {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT number, from_city, to_city, departure_time, arrival_time, duration, days, price, seats FROM schedule ORDER BY number";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rows.add(new Object[] {
                    rs.getString("number"),
                    rs.getString("from_city"),
                    rs.getString("to_city"),
                    rs.getString("departure_time"),
                    rs.getString("arrival_time"),
                    rs.getString("duration"),
                    rs.getString("days"),
                    rs.getString("price"),
                    rs.getInt("seats")
                });
            }
        } catch (SQLException e) {
            System.out.println("Ошибка получения расписания: " + e.getMessage());
        }
        return rows.toArray(new Object[0][]);
    }
    
    private Route mapRoute(ResultSet rs) throws SQLException {
        Route route = new Route();
        route.setId(rs.getString("id"));
        route.setFrom(rs.getString("from_city"));
        route.setTo(rs.getString("to_city"));
        route.setStops(rs.getString("stops"));
        route.setDepartureTime(rs.getString("departure_time"));
        route.setArrivalTime(rs.getString("arrival_time"));
        route.setDate(rs.getString("date"));
        route.setPrice(rs.getDouble("price"));
        route.setTotalSeats(rs.getInt("total_seats"));
        return route;
    }
    
    private Ticket mapTicket(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getString("id"));
        ticket.setUserId(rs.getString("user_id"));
        ticket.setRouteId(rs.getString("route_id"));
        ticket.setSeatNumber(rs.getString("seat_number"));
        ticket.setPassengerName(rs.getString("passenger_name"));
        ticket.setPassengerEmail(rs.getString("passenger_email"));
        ticket.setPassengerPhone(rs.getString("passenger_phone"));
        ticket.setStatus(rs.getString("status"));
        ticket.setCreatedAt(rs.getString("created_at"));
        return ticket;
    }
    
    private List<Ticket> getTicketsForRoute(String routeId) throws SQLException {
        List<Ticket> result = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE route_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, routeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapTicket(rs));
                }
            }
        }
        return result;
    }
}