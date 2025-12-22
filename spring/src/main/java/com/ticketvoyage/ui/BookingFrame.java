package com.ticketvoyage.ui;

import com.ticketvoyage.model.*;
import com.ticketvoyage.service.AuthService;
import com.ticketvoyage.service.DataService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.UUID;

public class BookingFrame extends JFrame {
    private AuthService authService;
    private DataService dataService;
    private Route route;
    private String selectedSeat;
    private JTextField passengerNameField;
    private JTextField passengerEmailField;
    private JTextField passengerPhoneField;
    private JPanel seatsPanel;
    
    public BookingFrame(AuthService authService, DataService dataService, Route route) {
        this.authService = authService;
        this.dataService = dataService;
        this.route = route;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("TicketVoyage - Бронирование");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(new Color(249, 250, 251));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JButton backButton = new JButton("← Назад к поиску");
        backButton.setForeground(new Color(37, 99, 235));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> goBack());
        mainPanel.add(backButton, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(249, 250, 251));
        
        JPanel routePanel = createRouteInfoPanel();
        contentPanel.add(routePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel seatPanel = createSeatSelectionPanel();
        contentPanel.add(seatPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel passengerPanel = createPassengerInfoPanel();
        contentPanel.add(passengerPanel);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createRouteInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        
        JLabel title = new JLabel(route.getFrom() + " → " + route.getTo());
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(new Color(17, 24, 39));
        
        JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        detailsPanel.setBackground(Color.WHITE);
        
        JLabel dateLabel = new JLabel("📅 " + route.getDate());
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(55, 65, 81));
        
        JLabel timeLabel = new JLabel("🕐 " + route.getDepartureTime() + " - " + route.getArrivalTime());
        timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        timeLabel.setForeground(new Color(55, 65, 81));
        
        JLabel priceLabel = new JLabel("💰 " + String.format("%.0f₽", route.getPrice()));
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        priceLabel.setForeground(new Color(37, 99, 235));
        
        detailsPanel.add(dateLabel);
        detailsPanel.add(timeLabel);
        detailsPanel.add(priceLabel);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(detailsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSeatSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        
        JLabel title = new JLabel("Выберите место");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(new Color(17, 24, 39));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        seatsPanel = new JPanel(new GridLayout(0, 4, 10, 10));
        seatsPanel.setBackground(Color.WHITE);
        seatsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        generateSeats();
        
        panel.add(seatsPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        legendPanel.setBackground(Color.WHITE);
        
        JPanel freeLegend = createLegendItem(new Color(219, 234, 254), new Color(37, 99, 235), "Свободно");
        JPanel selectedLegend = createLegendItem(new Color(37, 99, 235), new Color(17, 24, 39), "Выбрано");
        JPanel occupiedLegend = createLegendItem(new Color(229, 231, 235), new Color(107, 114, 128), "Занято");
        
        legendPanel.add(freeLegend);
        legendPanel.add(selectedLegend);
        legendPanel.add(occupiedLegend);
        
        panel.add(legendPanel);
        
        return panel;
    }
    
    private void generateSeats() {
        seatsPanel.removeAll();
        
        int rows = (int) Math.ceil(route.getTotalSeats() / 4.0);
        char[] columns = {'A', 'B', 'C', 'D'};
        
        for (int row = 1; row <= rows; row++) {
            for (char col : columns) {
                String seatNumber = row + String.valueOf(col);
                int index = (row - 1) * 4 + (col - 'A');
                
                if (index < route.getTotalSeats()) {
                    JButton seatButton = new JButton(seatNumber);
                    seatButton.setFont(new Font("SansSerif", Font.BOLD, 12));
                    seatButton.setFocusPainted(false);
                    seatButton.setBorderPainted(true);
                    seatButton.setContentAreaFilled(true);
                    seatButton.setOpaque(true);
                    seatButton.setBorder(BorderFactory.createLineBorder(new Color(156, 163, 175), 1));
                    
                    boolean isAvailable = dataService.isSeatAvailable(route.getId(), seatNumber);
                    
                    if (!isAvailable) {
                        seatButton.setBackground(new Color(229, 231, 235));
                        seatButton.setForeground(new Color(107, 114, 128));
                        seatButton.setEnabled(false);
                    } else if (seatNumber.equals(selectedSeat)) {
                        seatButton.setBackground(new Color(37, 99, 235));
                        seatButton.setForeground(Color.WHITE);
                        seatButton.setBorder(BorderFactory.createLineBorder(new Color(29, 78, 216), 2));
                    } else {
                        seatButton.setBackground(new Color(219, 234, 254));
                        seatButton.setForeground(new Color(37, 99, 235));
                    }
                    
                    if (isAvailable) {
                        seatButton.addMouseListener(new MouseAdapter() {
                            public void mouseEntered(MouseEvent e) {
                                if (!seatNumber.equals(selectedSeat)) {
                                    seatButton.setBackground(new Color(191, 219, 254));
                                    seatButton.setForeground(new Color(29, 78, 216));
                                }
                            }
                            public void mouseExited(MouseEvent e) {
                                if (!seatNumber.equals(selectedSeat)) {
                                    seatButton.setBackground(new Color(219, 234, 254));
                                    seatButton.setForeground(new Color(37, 99, 235));
                                }
                            }
                        });
                        
                        seatButton.addActionListener(e -> selectSeat(seatNumber));
                    }
                    
                    seatsPanel.add(seatButton);
                } else {
                    seatsPanel.add(new JLabel());
                }
            }
        }
        
        seatsPanel.revalidate();
        seatsPanel.repaint();
    }
    
    private JPanel createLegendItem(Color bgColor, Color textColor, String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(Color.WHITE);
        
        JLabel colorLabel = new JLabel();
        colorLabel.setOpaque(true);
        colorLabel.setBackground(bgColor);
        colorLabel.setPreferredSize(new Dimension(20, 20));
        colorLabel.setBorder(BorderFactory.createLineBorder(new Color(156, 163, 175)));
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        textLabel.setForeground(textColor);
        
        panel.add(colorLabel);
        panel.add(textLabel);
        
        return panel;
    }
    
    private JPanel createPassengerInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        
        JLabel title = new JLabel("Данные пассажира");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(new Color(17, 24, 39));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        passengerNameField = createFormField("ФИО", "Иванов Иван Иванович");
        passengerEmailField = createFormField("Email", "ivanov@example.com");
        passengerPhoneField = createFormField("Телефон", "+7 (900) 123-45-67");
        
        formPanel.add(createFieldPanel("ФИО*", passengerNameField));
        formPanel.add(createFieldPanel("Email*", passengerEmailField));
        formPanel.add(createFieldPanel("Телефон", passengerPhoneField));
        
        panel.add(formPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JButton bookButton = new JButton("Забронировать за " + String.format("%.0f₽", route.getPrice()));
        bookButton.setBackground(new Color(34, 197, 94));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        bookButton.setFocusPainted(false);
        bookButton.setBorderPainted(false);
        bookButton.setOpaque(true);
        bookButton.setContentAreaFilled(true);
        bookButton.setPreferredSize(new Dimension(300, 45));
        bookButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        bookButton.addActionListener(e -> bookTicket());
        
        bookButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                bookButton.setBackground(new Color(22, 163, 74));
            }
            public void mouseExited(MouseEvent e) {
                bookButton.setBackground(new Color(34, 197, 94));
            }
        });
        
        panel.add(bookButton);
        
        return panel;
    }
    
    private JTextField createFormField(String label, String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(new Color(17, 24, 39));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        return field;
    }
    
    private JPanel createFieldPanel(String label, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("SansSerif", Font.BOLD, 12));
        labelComponent.setForeground(new Color(17, 24, 39));
        labelComponent.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(400, 40));
        
        panel.add(labelComponent);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        
        return panel;
    }
    
    private void selectSeat(String seatNumber) {
        selectedSeat = seatNumber;
        generateSeats();
    }
    
    private void bookTicket() {
        if (selectedSeat == null) {
            JOptionPane.showMessageDialog(this, "Выберите место", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String name = passengerNameField.getText().trim();
        String email = passengerEmailField.getText().trim();
        String phone = passengerPhoneField.getText().trim();
        
        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Заполните обязательные поля (ФИО и Email)", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!dataService.isSeatAvailable(route.getId(), selectedSeat)) {
            JOptionPane.showMessageDialog(this, "Место уже занято. Пожалуйста, выберите другое место.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            generateSeats();
            return;
        }
        
        Ticket ticket = new Ticket(
            UUID.randomUUID().toString(),
            authService.getCurrentUser().getId(),
            route.getId(),
            selectedSeat,
            name,
            email,
            phone,
            "BOOKED"
        );
        
        dataService.addTicket(ticket);
        
        JOptionPane.showMessageDialog(this, 
            "Билет успешно забронирован!\n\n" +
            "Маршрут: " + route.getFrom() + " - " + route.getTo() + "\n" +
            "Дата: " + route.getDate() + "\n" +
            "Время: " + route.getDepartureTime() + "\n" +
            "Место: " + selectedSeat + "\n" +
            "Пассажир: " + name,
            "Бронирование подтверждено", 
            JOptionPane.INFORMATION_MESSAGE);
        
        goBack();
    }
    
    private void goBack() {
        SwingUtilities.invokeLater(() -> {
            new MainFrame(authService, dataService).setVisible(true);
            dispose();
        });
    }
}