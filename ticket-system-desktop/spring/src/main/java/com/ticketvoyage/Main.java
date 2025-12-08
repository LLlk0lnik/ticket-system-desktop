package com.ticketvoyage;

import com.ticketvoyage.service.DataService;
import com.ticketvoyage.ui.LoginFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        DataService dataService = new DataService();
        
        SwingUtilities.invokeLater(() -> {
            new LoginFrame(dataService).setVisible(true);
        });
    }
}