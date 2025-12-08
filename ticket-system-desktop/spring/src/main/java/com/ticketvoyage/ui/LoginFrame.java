package com.ticketvoyage.ui;

import com.ticketvoyage.service.AuthService;
import com.ticketvoyage.service.DataService;
import com.ticketvoyage.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private AuthService authService;
    private DataService dataService;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JButton submitButton;
    private JButton switchModeButton;
    private boolean isLoginMode = true;
    
    public LoginFrame(DataService dataService) {
        this.dataService = dataService;
        this.authService = new AuthService(dataService);
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("TicketVoyage - Вход");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                Color color1 = new Color(219, 234, 254);
                Color color2 = new Color(199, 210, 254);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new FlowLayout());
        
        JLabel logo = new JLabel("🎫 TicketVoyage");
        logo.setFont(new Font("SansSerif", Font.BOLD, 24));
        logo.setForeground(new Color(30, 58, 138));
        headerPanel.add(logo);
        
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        
        JLabel titleLabel = new JLabel("Вход в систему");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 58, 138));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(titleLabel, gbc);
        
        nameField = createTextField("Иван Иванов", "Имя");
        JLabel nameLabel = new JLabel("Имя:");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        nameLabel.setForeground(new Color(30, 58, 138));
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 0, 5, 5);
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(nameField, gbc);
        
        emailField = createTextField("user@example.com", "Email");
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        emailLabel.setForeground(new Color(30, 58, 138));
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(emailField, gbc);
        
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(250, 40));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(156, 163, 175)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JLabel passwordLabel = new JLabel("Пароль:");
        passwordLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        passwordLabel.setForeground(new Color(30, 58, 138));
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(passwordField, gbc);
        
        submitButton = new JButton("Войти");
        submitButton.setBackground(new Color(37, 99, 235));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setPreferredSize(new Dimension(250, 45));
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        formPanel.add(submitButton, gbc);
        
        switchModeButton = new JButton("Нет аккаунта? Зарегистрироваться");
        switchModeButton.setForeground(new Color(37, 99, 235));
        switchModeButton.setBorderPainted(false);
        switchModeButton.setContentAreaFilled(false);
        switchModeButton.setFocusPainted(false);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 0, 0, 0);
        formPanel.add(switchModeButton, gbc);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        
        setupEventListeners();
        updateFormForMode();
    }
    
    private JTextField createTextField(String placeholder, String tooltip) {
        JTextField field = new JTextField(placeholder);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(250, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(156, 163, 175)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        field.setToolTipText(tooltip);
        return field;
    }
    
    private void setupEventListeners() {
        submitButton.addActionListener(this::handleSubmit);
        switchModeButton.addActionListener(e -> switchMode());
        
        emailField.addActionListener(this::handleSubmit);
        passwordField.addActionListener(this::handleSubmit);
        nameField.addActionListener(this::handleSubmit);
    }
    
    private void handleSubmit(ActionEvent e) {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String name = nameField.getText().trim();
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Заполните все поля", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!isLoginMode && name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите имя", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        User user;
        if (isLoginMode) {
            user = authService.login(email, password);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Неверный email или пароль", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            user = authService.register(email, password, name);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Пользователь с таким email уже существует", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        SwingUtilities.invokeLater(() -> {
            new MainFrame(authService, dataService).setVisible(true);
            dispose();
        });
    }
    
    private void switchMode() {
        isLoginMode = !isLoginMode;
        updateFormForMode();
    }
    
    private void updateFormForMode() {
        if (isLoginMode) {
            setTitle("TicketVoyage - Вход");
            submitButton.setText("Войти");
            switchModeButton.setText("Нет аккаунта? Зарегистрироваться");
            nameField.setVisible(false);
            Component[] components = ((JPanel)nameField.getParent()).getComponents();
            for (Component comp : components) {
                if (comp instanceof JLabel) {
                    JLabel label = (JLabel) comp;
                    if ("Имя:".equals(label.getText())) {
                        label.setVisible(false);
                        break;
                    }
                }
            }
        } else {
            setTitle("TicketVoyage - Регистрация");
            submitButton.setText("Зарегистрироваться");
            switchModeButton.setText("Уже есть аккаунт? Войти");
            nameField.setVisible(true);
            Component[] components = ((JPanel)nameField.getParent()).getComponents();
            for (Component comp : components) {
                if (comp instanceof JLabel) {
                    JLabel label = (JLabel) comp;
                    if ("Имя:".equals(label.getText())) {
                        label.setVisible(true);
                        break;
                    }
                }
            }
        }
        revalidate();
        repaint();
    }
}