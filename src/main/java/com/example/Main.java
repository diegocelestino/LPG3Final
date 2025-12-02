package com.example;

import com.example.config.FirebaseConfig;
import com.example.gui.MainWindow;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FirebaseConfig.initialize();
        
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}
