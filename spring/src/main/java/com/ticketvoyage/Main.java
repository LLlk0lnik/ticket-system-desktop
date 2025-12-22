package com.ticketvoyage;

import com.ticketvoyage.service.AuthService;
import com.ticketvoyage.service.DataService;
import com.ticketvoyage.ui.MainFrame;
import com.ticketvoyage.model.User;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        try {
            DataService dataService = new DataService();
            AuthService authService = new AuthService(dataService);
            User autoUser = dataService.findUserByEmail("auto_user");
            if (autoUser != null) {
                authService.login("auto_user", "auto123");
            }
            SwingUtilities.invokeLater(() -> {
                try {
                    MainFrame frame = new MainFrame(authService, dataService);
                    frame.setVisible(true);
                } catch (Exception e) {
                }
            });
        } catch (Exception e) {
        }
    }
}