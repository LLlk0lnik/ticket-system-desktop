package com.ticketvoyage.ui;

import com.ticketvoyage.ScheduleFrame;
import com.ticketvoyage.model.*;
import com.ticketvoyage.service.AuthService;
import com.ticketvoyage.service.DataService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class MainFrame extends JFrame {
    private AuthService authService;
    private DataService dataService;
    private JTextField searchFromField;
    private JTextField searchToField;
    private JTextField searchDateField;
    private JPanel routesPanel;
    private JComboBox<String> sortComboBox;
    private List<Route> currentRoutes;
    
    public MainFrame(AuthService authService, DataService dataService) {
        this.authService = authService;
        this.dataService = dataService;
        initializeUI();
        loadRoutes();
    }
    
    private void initializeUI() {
        setTitle("TicketVoyage - Поиск билетов");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(249, 250, 251));
        
        JPanel navBar = createNavigationBar();
        mainPanel.add(navBar, BorderLayout.NORTH);
        
        JPanel contentPanel = createContentPanel();
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
        
        JButton searchBtn = createNavButton("Поиск билетов", true);
        JButton ticketsBtn = createNavButton("Мои билеты", false);
        
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
        ticketsBtn.addActionListener(e -> showProfileView());
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
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(new Color(249, 250, 251));
        contentPanel.setBorder(new EmptyBorder(30, 50, 30, 50));
        
        JPanel searchPanel = createSearchPanel();
        
        JScrollPane scrollPane = createResultsPanel();
        
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel title = new JLabel("Поиск билетов");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(17, 24, 39));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchPanel.add(title);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel fieldsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        fieldsPanel.setBackground(Color.WHITE);
        fieldsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        fieldsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        searchFromField = createTextField("Москва", "Откуда");
        searchToField = createTextField("Санкт-Петербург", "Куда");
        searchDateField = createTextField("2025-10-27", "Дата (ГГГГ-ММ-ДД)");
        
        fieldsPanel.add(createFieldPanel("Откуда", searchFromField));
        fieldsPanel.add(createFieldPanel("Куда", searchToField));
        fieldsPanel.add(createFieldPanel("Дата", searchDateField));
        
        searchPanel.add(fieldsPanel);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchPanel.add(separator);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JPanel filtersPanel = createFiltersPanel();
        searchPanel.add(filtersPanel);
        
        return searchPanel;
    }
    
    private JPanel createFieldPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(new Color(17, 24, 39));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(textField);
        
        return panel;
    }
    
    private JTextField createTextField(String placeholder, String tooltip) {
        JTextField field = new JTextField(placeholder);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(new Color(17, 24, 39));
        field.setPreferredSize(new Dimension(0, 45));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setToolTipText(tooltip);
        return field;
    }
    
    private JPanel createFiltersPanel() {
        JPanel filtersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filtersPanel.setBackground(Color.WHITE);
        filtersPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        filtersPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel filterLabel = new JLabel("Сортировка:");
        filterLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        filterLabel.setForeground(new Color(17, 24, 39));
        filtersPanel.add(filterLabel);
        
        String[] sortOptions = {
            "По умолчанию",
            "Цена (по возрастанию)",
            "Цена (по убыванию)",
            "Время отправления",
            "Длительность"
        };
        sortComboBox = new JComboBox<>(sortOptions);
        sortComboBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sortComboBox.addActionListener(e -> filterAndSortRoutes());
        filtersPanel.add(sortComboBox);
        
        JButton searchButton = new JButton("Найти");
        searchButton.setBackground(new Color(37, 99, 235));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(false);
        searchButton.setOpaque(true);
        searchButton.setContentAreaFilled(true);
        searchButton.setPreferredSize(new Dimension(100, 35));
        searchButton.addActionListener(e -> filterAndSortRoutes());
        filtersPanel.add(searchButton);

        JButton scheduleButton = new JButton("Расписание рейсов");
        scheduleButton.setBackground(new Color(37, 99, 235));
        scheduleButton.setForeground(Color.WHITE);
        scheduleButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        scheduleButton.setFocusPainted(false);
        scheduleButton.setBorderPainted(false);
        scheduleButton.setOpaque(true);
        scheduleButton.setContentAreaFilled(true);
        scheduleButton.setPreferredSize(new Dimension(160, 35));
        scheduleButton.addActionListener(e -> new ScheduleFrame().setVisible(true));
        filtersPanel.add(scheduleButton);
        
        JButton clearBtn = new JButton("Очистить");
        clearBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        clearBtn.setForeground(new Color(220, 38, 38));
        clearBtn.setBackground(Color.WHITE);
        clearBtn.setBorderPainted(false);
        clearBtn.setFocusPainted(false);
        clearBtn.setOpaque(false);
        clearBtn.addActionListener(e -> clearFilters());
        filtersPanel.add(clearBtn);
        
        return filtersPanel;
    }
    
    private JScrollPane createResultsPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(249, 250, 251));
        mainPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        JLabel resultsTitle = new JLabel("Доступные рейсы");
        resultsTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        resultsTitle.setForeground(new Color(17, 24, 39));
        resultsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(resultsTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        routesPanel = new JPanel();
        routesPanel.setLayout(new BoxLayout(routesPanel, BoxLayout.Y_AXIS));
        routesPanel.setBackground(new Color(249, 250, 251));
        routesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        mainPanel.add(routesPanel);
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        return scrollPane;
    }
    
    private void loadRoutes() {
        currentRoutes = dataService.getAllRoutes();
        displayRoutes(currentRoutes);
    }
    
    private void displayRoutes(List<Route> routes) {
        routesPanel.removeAll();
        
        JLabel countLabel = new JLabel("Найдено рейсов: " + routes.size());
        countLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        countLabel.setForeground(new Color(55, 65, 81));
        countLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        routesPanel.add(countLabel);
        routesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        if (routes.isEmpty()) {
            JLabel noResults = new JLabel("Рейсов не найдено. Попробуйте изменить параметры поиска.");
            noResults.setFont(new Font("SansSerif", Font.PLAIN, 14));
            noResults.setForeground(new Color(107, 114, 128));
            noResults.setAlignmentX(Component.LEFT_ALIGNMENT);
            routesPanel.add(noResults);
        } else {
            for (Route route : routes) {
                routesPanel.add(createRouteCard(route));
                routesPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }
        
        routesPanel.revalidate();
        routesPanel.repaint();
    }
    
    private JPanel createRouteCard(Route route) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(20, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(147, 197, 253), 2),
                    BorderFactory.createEmptyBorder(19, 24, 19, 24)
                ));
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                    BorderFactory.createEmptyBorder(20, 25, 20, 25)
                ));
            }
        });
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        timePanel.setBackground(Color.WHITE);
        
        JPanel depPanel = createTimePanel(route.getDepartureTime(), route.getFrom());
        JLabel arrow = new JLabel("→");
        arrow.setFont(new Font("SansSerif", Font.PLAIN, 24));
        arrow.setForeground(new Color(156, 163, 175));
        JPanel arrPanel = createTimePanel(route.getArrivalTime(), route.getTo());
        
        timePanel.add(depPanel);
        timePanel.add(arrow);
        timePanel.add(arrPanel);
        
        infoPanel.add(timePanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        detailsPanel.setBackground(Color.WHITE);
        
        JLabel dateLabel = new JLabel("📅 " + route.getDate());
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        dateLabel.setForeground(new Color(55, 65, 81));
        
        int freeSeats = route.getTotalSeats() - route.getBookedSeats();
        JLabel seatsLabel = new JLabel("💺 Свободно мест: " + freeSeats + " из " + route.getTotalSeats());
        seatsLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        seatsLabel.setForeground(new Color(55, 65, 81));
        
        if (route.getStops() != null && !route.getStops().isEmpty()) {
            JLabel stopsLabel = new JLabel("🛑 Остановки: " + route.getStops());
            stopsLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
            stopsLabel.setForeground(new Color(55, 65, 81));
            detailsPanel.add(stopsLabel);
        }
        
        detailsPanel.add(dateLabel);
        detailsPanel.add(seatsLabel);
        
        infoPanel.add(detailsPanel);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        
        JLabel priceLabel = new JLabel(String.format("%.0f₽", route.getPrice()));
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        priceLabel.setForeground(new Color(37, 99, 235));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton selectBtn = new JButton("Выбрать место");
        selectBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        selectBtn.setBackground(new Color(37, 99, 235));
        selectBtn.setForeground(Color.WHITE);
        selectBtn.setFocusPainted(false);
        selectBtn.setBorderPainted(false);
        selectBtn.setPreferredSize(new Dimension(150, 40));
        selectBtn.setMaximumSize(new Dimension(150, 40));
        selectBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectBtn.setOpaque(true);

        selectBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                selectBtn.setBackground(new Color(29, 78, 216));
                selectBtn.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e) {
                selectBtn.setBackground(new Color(37, 99, 235));
                selectBtn.setForeground(Color.WHITE);
            }
        });

        selectBtn.addActionListener(e -> openBooking(route));
        
        rightPanel.add(priceLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(selectBtn);
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private JPanel createTimePanel(String time, String location) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        timeLabel.setForeground(new Color(17, 24, 39));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel locationLabel = new JLabel(location);
        locationLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        locationLabel.setForeground(new Color(55, 65, 81));
        locationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(timeLabel);
        panel.add(locationLabel);
        
        return panel;
    }
    
    private void filterAndSortRoutes() {
        String from = searchFromField.getText().trim();
        String to = searchToField.getText().trim();
        String date = searchDateField.getText().trim();
        
        List<Route> filtered = dataService.getAllRoutes().stream()
            .filter(route -> from.isEmpty() || route.getFrom().toLowerCase().contains(from.toLowerCase()))
            .filter(route -> to.isEmpty() || route.getTo().toLowerCase().contains(to.toLowerCase()))
            .filter(route -> date.isEmpty() || route.getDate().equals(date))
            .collect(Collectors.toList());
        
        String sortOption = (String) sortComboBox.getSelectedItem();
        if (sortOption != null) {
            switch (sortOption) {
                case "Цена (по возрастанию)":
                    filtered.sort((r1, r2) -> Double.compare(r1.getPrice(), r2.getPrice()));
                    break;
                case "Цена (по убыванию)":
                    filtered.sort((r1, r2) -> Double.compare(r2.getPrice(), r1.getPrice()));
                    break;
                case "Время отправления":
                    filtered.sort((r1, r2) -> r1.getDepartureTime().compareTo(r2.getDepartureTime()));
                    break;
                case "Длительность":
                    filtered.sort((r1, r2) -> {
                        int duration1 = calculateDuration(r1.getDepartureTime(), r1.getArrivalTime());
                        int duration2 = calculateDuration(r2.getDepartureTime(), r2.getArrivalTime());
                        return Integer.compare(duration1, duration2);
                    });
                    break;
            }
        }
        
        currentRoutes = filtered;
        displayRoutes(filtered);
    }
    
    private int calculateDuration(String depTime, String arrTime) {
        String[] dep = depTime.split(":");
        String[] arr = arrTime.split(":");
        int depMinutes = Integer.parseInt(dep[0]) * 60 + Integer.parseInt(dep[1]);
        int arrMinutes = Integer.parseInt(arr[0]) * 60 + Integer.parseInt(arr[1]);
        if (arrMinutes < depMinutes) {
            arrMinutes += 24 * 60;
        }
        return arrMinutes - depMinutes;
    }
    
    private void clearFilters() {
        searchFromField.setText("");
        searchToField.setText("");
        searchDateField.setText("");
        sortComboBox.setSelectedIndex(0);
        loadRoutes();
    }
    
    private void openBooking(Route route) {
        SwingUtilities.invokeLater(() -> {
            new BookingFrame(authService, dataService, route).setVisible(true);
            dispose();
        });
    }
    
        private void showSearchView() {
    }
    
    private void showProfileView() {
        SwingUtilities.invokeLater(() -> {
            new ProfileFrame(authService, dataService).setVisible(true);
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