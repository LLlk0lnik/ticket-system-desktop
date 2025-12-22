package com.ticketvoyage.ui;

import com.ticketvoyage.model.*;
import com.ticketvoyage.service.AuthService;
import com.ticketvoyage.service.DataService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ProfileFrame extends JFrame {
    private AuthService authService;
    private DataService dataService;
    
    public ProfileFrame(AuthService authService, DataService dataService) {
        this.authService = authService;
        this.dataService = dataService;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("TicketVoyage - Все билеты");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(249, 250, 251));

        JPanel navBar = createNavigationBar();
        mainPanel.add(navBar, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(249, 250, 251));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Все билеты");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(17, 24, 39));
        contentPanel.add(title, BorderLayout.NORTH);
        
        JScrollPane ticketsScrollPane = createTicketsPanel();
        contentPanel.add(ticketsScrollPane, BorderLayout.CENTER);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createNavigationBar() {
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(Color.WHITE);
        navBar.setPreferredSize(new Dimension(0, 60));
        navBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        leftPanel.setBackground(Color.WHITE);
        
        JLabel logo = new JLabel("🎫 TicketVoyage");
        logo.setFont(new Font("SansSerif", Font.BOLD, 20));
        logo.setForeground(new Color(17, 24, 39));
        leftPanel.add(logo);
        
        JButton searchBtn = createNavButton("Поиск билетов", false);
        JButton ticketsBtn = createNavButton("Все билеты", true);
        
        leftPanel.add(searchBtn);
        leftPanel.add(ticketsBtn);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        rightPanel.setBackground(Color.WHITE);
        
        User currentUser = authService.getCurrentUser();
        JLabel userLabel = new JLabel("👤 " + (currentUser.getName() != null ? currentUser.getName() : currentUser.getEmail()));
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        userLabel.setForeground(new Color(55, 65, 81));
        
        JButton logoutBtn = new JButton("Выйти");
        logoutBtn.setBackground(new Color(239, 68, 68));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setPreferredSize(new Dimension(80, 35));
        logoutBtn.setOpaque(true);
        logoutBtn.setContentAreaFilled(true);
        
        rightPanel.add(userLabel);
        rightPanel.add(logoutBtn);
        
        navBar.add(leftPanel, BorderLayout.WEST);
        navBar.add(rightPanel, BorderLayout.EAST);
        
        searchBtn.addActionListener(e -> showSearchView());
        logoutBtn.addActionListener(e -> logout());
        
        return navBar;
    }
    
    private JButton createNavButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        
        if (active) {
            btn.setForeground(new Color(37, 99, 235));
            btn.setBackground(new Color(219, 234, 254));
            btn.setOpaque(true);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        } else {
            btn.setForeground(new Color(55, 65, 81));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        }
        
        return btn;
    }
    
    private JScrollPane createTicketsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(249, 250, 251));

        List<Ticket> allTickets = dataService.getAllTickets();

        if (allTickets.isEmpty()) {
            JLabel noTicketsLabel = new JLabel("Пока нет забронированных билетов");
            noTicketsLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            noTicketsLabel.setForeground(new Color(107, 114, 128));
            noTicketsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(Box.createVerticalGlue());
            panel.add(noTicketsLabel);
            panel.add(Box.createVerticalGlue());
        } else {
            for (Ticket ticket : allTickets) {
                Route route = dataService.findRouteById(ticket.getRouteId());
                if (route != null) {
                    panel.add(createTicketCard(ticket, route));
                    panel.add(Box.createRigidArea(new Dimension(0, 15)));
                }
            }
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }
    
    private JPanel createTicketCard(Ticket ticket, Route route) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel routeLabel = new JLabel(route.getFrom() + " → " + route.getTo());
        routeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        routeLabel.setForeground(new Color(17, 24, 39));
        
        JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        detailsPanel.setBackground(Color.WHITE);
        
        JLabel dateLabel = new JLabel("📅 " + route.getDate());
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JLabel timeLabel = new JLabel("🕐 " + route.getDepartureTime() + " - " + route.getArrivalTime());
        timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JLabel seatLabel = new JLabel("💺 Место: " + ticket.getSeatNumber());
        seatLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JLabel passengerLabel = new JLabel("👤 " + ticket.getPassengerName());
        passengerLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JLabel createdLabel = new JLabel("🕒 " + ticket.getCreatedAt());
        createdLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        createdLabel.setForeground(new Color(107, 114, 128));
        
        detailsPanel.add(dateLabel);
        detailsPanel.add(timeLabel);
        detailsPanel.add(seatLabel);
        detailsPanel.add(passengerLabel);
        
        infoPanel.add(routeLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(detailsPanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(createdLabel);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        
        JLabel priceLabel = new JLabel(String.format("%.0f₽", route.getPrice()));
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        priceLabel.setForeground(new Color(37, 99, 235));
        priceLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JLabel statusLabel = new JLabel(getStatusText(ticket.getStatus()));
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        statusLabel.setForeground(getStatusColor(ticket.getStatus()));
        statusLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        rightPanel.add(priceLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(statusLabel);
        
        if ("BOOKED".equals(ticket.getStatus())) {
            rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            JButton cancelButton = new JButton("Отменить бронь");
            cancelButton.setBackground(new Color(239, 68, 68));
            cancelButton.setForeground(Color.WHITE);
            cancelButton.setFont(new Font("SansSerif", Font.BOLD, 12));
            cancelButton.setFocusPainted(false);
            cancelButton.setBorderPainted(false);
            cancelButton.setOpaque(true);
            cancelButton.setContentAreaFilled(true);
            cancelButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
            cancelButton.addActionListener(e -> cancelTicket(ticket));
            rightPanel.add(cancelButton);
        }
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private String getStatusText(String status) {
        switch (status) {
            case "BOOKED": return "АКТИВЕН";
            case "CANCELLED": return "ОТМЕНЕН";
            case "USED": return "ИСПОЛЬЗОВАН";
            default: return status;
        }
    }
    
    private Color getStatusColor(String status) {
        switch (status) {
            case "BOOKED": return new Color(34, 197, 94);
            case "CANCELLED": return new Color(239, 68, 68);
            case "USED": return new Color(107, 114, 128);
            default: return Color.BLACK;
        }
    }
    
    private void cancelTicket(Ticket ticket) {
        int result = JOptionPane.showConfirmDialog(this,
            "Вы уверены, что хотите отменить бронирование?\n\n" +
            "Маршрут: " + dataService.findRouteById(ticket.getRouteId()).getFrom() + " - " + 
            dataService.findRouteById(ticket.getRouteId()).getTo() + "\n" +
            "Место: " + ticket.getSeatNumber() + "\n" +
            "Пассажир: " + ticket.getPassengerName(),
            "Подтверждение отмены",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (result == JOptionPane.YES_OPTION) {
            dataService.cancelTicket(ticket.getId());
            JOptionPane.showMessageDialog(this, "Бронирование отменено", "Успех", JOptionPane.INFORMATION_MESSAGE);
            initializeUI();
        }
    }
    
    private void showSearchView() {
        SwingUtilities.invokeLater(() -> {
            new MainFrame(authService, dataService).setVisible(true);
            dispose();
        });
    }
    
    private void logout() {
        authService.logout();
        SwingUtilities.invokeLater(() -> {
            new LoginFrame(dataService).setVisible(true);
            dispose();
        });
    }
}