package com.example.gui;

import javax.swing.*;
import java.awt.*;

public class Toast extends JDialog {
    
    public Toast(JFrame parent, String message) {
        super(parent);
        setUndecorated(true);
        setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(50, 50, 50, 230));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel label = new JLabel(message);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(label);
        
        add(panel);
        pack();
        
        if (parent != null) {
            int x = parent.getX() + (parent.getWidth() - getWidth()) / 2;
            int y = parent.getY() + parent.getHeight() - getHeight() - 50;
            setLocation(x, y);
        }
        
        setOpacity(0.9f);
    }
    
    public void showToast(int durationMs) {
        setVisible(true);
        
        Timer timer = new Timer(durationMs, e -> {
            setVisible(false);
            dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    public static void show(JFrame parent, String message, int durationMs) {
        SwingUtilities.invokeLater(() -> {
            Toast toast = new Toast(parent, message);
            toast.showToast(durationMs);
        });
    }
    
    public static void show(JFrame parent, String message) {
        show(parent, message, 2000);
    }
}
