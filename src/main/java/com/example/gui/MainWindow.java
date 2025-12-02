package com.example.gui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    
    public MainWindow() {
        setTitle("API Client Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title Label
        JLabel titleLabel = new JLabel("Welcome to Client Manager", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JButton registerButton = new JButton("Register");
        JButton dataButton = new JButton("Data");
        
        registerButton.setPreferredSize(new Dimension(200, 50));
        dataButton.setPreferredSize(new Dimension(200, 50));
        
        registerButton.addActionListener(e -> openRegisterPage());
        dataButton.addActionListener(e -> openDataPage());
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(registerButton, gbc);
        
        gbc.gridy = 1;
        buttonPanel.add(dataButton, gbc);
        
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void openRegisterPage() {
        RegisterWindow registerWindow = new RegisterWindow();
        registerWindow.setVisible(true);
        this.dispose();
    }
    
    private void openDataPage() {
        DataWindow dataWindow = new DataWindow();
        dataWindow.setVisible(true);
        this.dispose();
    }
}
