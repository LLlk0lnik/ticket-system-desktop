package com.ticketvoyage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicketVoyageApp extends JFrame {
    
    private JTextField searchFromField;
    private JTextField searchToField;
    private JTextField searchDateField;
    private JPanel routesPanel;
    private ButtonGroup filterGroup;
    private JToggleButton priceFilterBtn;
    private JToggleButton timeFilterBtn;
    private JToggleButton durationFilterBtn;
    
    public TicketVoyageApp() {
        setTitle("TicketVoyage - Поиск билетов");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(249, 250, 251));
        
        JPanel navBar = createNavigationBar();
        mainPanel.add(navBar, BorderLayout.NORTH);
        
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createNavigationBar() {
        JPanel navBar = new JPanel();
        navBar.setLayout(new BorderLayout());
        navBar.setBackground(Color.WHITE);
        navBar.setPreferredSize(new Dimension(0, 70));
        navBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        leftPanel.setBackground(Color.WHITE);
        
        JLabel logo = new JLabel("🎫 TicketVoyage");
        logo.setFont(new Font("SansSerif", Font.BOLD, 20));
        logo.setForeground(new Color(17, 24, 39));
        leftPanel.add(logo);
        
        JButton searchBtn = createNavButton("Поиск билетов", true);
        JButton ticketsBtn = createNavButton("Мои билеты", false);
        JButton scheduleBtn = createNavButton("Расписание", false);
        
        scheduleBtn.addActionListener(e -> {
            new ScheduleFrame().setVisible(true);
        });
        
        leftPanel.add(searchBtn);
        leftPanel.add(ticketsBtn);
        leftPanel.add(scheduleBtn);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        rightPanel.setBackground(Color.WHITE);
        
        JLabel userLabel = new JLabel("👤 user@example.com");
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        userLabel.setForeground(new Color(55, 65, 81));
        
        JButton logoutBtn = new JButton("Выйти");
        logoutBtn.setBackground(new Color(239, 68, 68));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(80, 35));
        logoutBtn.setOpaque(true);
        
        rightPanel.add(userLabel);
        rightPanel.add(logoutBtn);
        
        navBar.add(leftPanel, BorderLayout.WEST);
        navBar.add(rightPanel, BorderLayout.EAST);
        
        return navBar;
    }
    
    private JButton createNavButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0, 20));
        contentPanel.setBackground(new Color(249, 250, 251));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
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
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel title = new JLabel("Поиск билетов");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(17, 24, 39));
        headerPanel.add(title, BorderLayout.WEST);
        
        JButton scheduleHeaderBtn = new JButton("📅 Расписание рейсов");
        scheduleHeaderBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        scheduleHeaderBtn.setForeground(Color.WHITE);
        scheduleHeaderBtn.setBackground(new Color(16, 185, 129));
        scheduleHeaderBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        scheduleHeaderBtn.setFocusPainted(false);
        scheduleHeaderBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        scheduleHeaderBtn.setOpaque(true);
        
        scheduleHeaderBtn.addActionListener(e -> {
            new ScheduleFrame().setVisible(true);
        });
        
        scheduleHeaderBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                scheduleHeaderBtn.setBackground(new Color(5, 150, 105));
            }
            public void mouseExited(MouseEvent e) {
                scheduleHeaderBtn.setBackground(new Color(16, 185, 129));
            }
        });
        
        headerPanel.add(scheduleHeaderBtn, BorderLayout.EAST);
        
        searchPanel.add(headerPanel);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel fieldsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        fieldsPanel.setBackground(Color.WHITE);
        fieldsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        fieldsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        searchFromField = createTextField("Москва", "Откуда");
        searchToField = createTextField("Санкт-Петербург", "Куда");
        searchDateField = createTextField("2025-10-27", "Дата");
        
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
        
        priceFilterBtn = createFilterButton("💰 Цена");
        timeFilterBtn = createFilterButton("🕐 Время");
        durationFilterBtn = createFilterButton("⏱️ Длительность");
        
        filtersPanel.add(priceFilterBtn);
        filtersPanel.add(timeFilterBtn);
        filtersPanel.add(durationFilterBtn);
        
        filtersPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        
        JButton scheduleBtn = new JButton("📅 Расписание рейсов");
        scheduleBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        scheduleBtn.setForeground(Color.WHITE);
        scheduleBtn.setBackground(new Color(16, 185, 129));
        scheduleBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        scheduleBtn.setFocusPainted(false);
        scheduleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        scheduleBtn.setOpaque(true);
        
        scheduleBtn.addActionListener(e -> {
            new ScheduleFrame().setVisible(true);
        });
        
        scheduleBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                scheduleBtn.setBackground(new Color(5, 150, 105));
            }
            public void mouseExited(MouseEvent e) {
                scheduleBtn.setBackground(new Color(16, 185, 129));
            }
        });
        
        filtersPanel.add(scheduleBtn);
        
        JButton clearBtn = new JButton("✕ Очистить всё");
        clearBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        clearBtn.setForeground(new Color(220, 38, 38));
        clearBtn.setBackground(Color.WHITE);
        clearBtn.setBorderPainted(false);
        clearBtn.setFocusPainted(false);
        clearBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearBtn.setOpaque(false);
        clearBtn.setContentAreaFilled(false);
        filtersPanel.add(clearBtn);
        
        return filtersPanel;
    }
    
    private JToggleButton createFilterButton(String text) {
        JToggleButton btn = new JToggleButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(new Color(243, 244, 246));
        btn.setForeground(new Color(17, 24, 39));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        
        btn.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                btn.setBackground(new Color(37, 99, 235));
                btn.setForeground(Color.WHITE);
                btn.repaint();
            } else {
                btn.setBackground(new Color(243, 244, 246));
                btn.setForeground(new Color(17, 24, 39));
                btn.repaint();
            }
        });
        
        return btn;
    }
    
    private JScrollPane createResultsPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(249, 250, 251));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JLabel resultsTitle = new JLabel("Доступные рейсы (3)");
        resultsTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        resultsTitle.setForeground(new Color(17, 24, 39));
        resultsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(resultsTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        routesPanel = new JPanel();
        routesPanel.setLayout(new BoxLayout(routesPanel, BoxLayout.Y_AXIS));
        routesPanel.setBackground(new Color(249, 250, 251));
        routesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        routesPanel.add(createRouteCard("Москва", "Санкт-Петербург", "08:30", "12:45", "2025-10-27", "1500", 35, 40));
        routesPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        routesPanel.add(createRouteCard("Москва", "Санкт-Петербург", "15:00", "19:30", "2025-10-27", "1800", 20, 40));
        routesPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        routesPanel.add(createRouteCard("Москва", "Санкт-Петербург", "22:00", "02:15", "2025-10-27", "1200", 38, 40));
        
        mainPanel.add(routesPanel);
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        return scrollPane;
    }
    
    private JPanel createRouteCard(String from, String to, String depTime, String arrTime, 
                                   String date, String price, int freeSeats, int totalSeats) {
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
        
        JPanel depPanel = createTimePanel(depTime, from);
        JLabel arrow = new JLabel("→");
        arrow.setFont(new Font("SansSerif", Font.PLAIN, 24));
        arrow.setForeground(new Color(156, 163, 175));
        JPanel arrPanel = createTimePanel(arrTime, to);
        
        timePanel.add(depPanel);
        timePanel.add(arrow);
        timePanel.add(arrPanel);
        
        infoPanel.add(timePanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        detailsPanel.setBackground(Color.WHITE);
        
        JLabel dateLabel = new JLabel("📅 " + date);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        dateLabel.setForeground(new Color(55, 65, 81));
        
        JLabel seatsLabel = new JLabel("💺 Свободно мест: " + freeSeats + " из " + totalSeats);
        seatsLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        seatsLabel.setForeground(new Color(55, 65, 81));
        
        detailsPanel.add(dateLabel);
        detailsPanel.add(seatsLabel);
        
        infoPanel.add(detailsPanel);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        
        JLabel priceLabel = new JLabel(price + "₽");
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        priceLabel.setForeground(new Color(37, 99, 235));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton selectBtn = new JButton("Выбрать место");
        selectBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        selectBtn.setBackground(new Color(37, 99, 235));
        selectBtn.setForeground(Color.WHITE);
        selectBtn.setFocusPainted(false);
        selectBtn.setBorderPainted(false);
        selectBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            TicketVoyageApp app = new TicketVoyageApp();
            app.setVisible(true);
        });
    }
}

