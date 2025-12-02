package com.example.gui;

import com.example.model.Address;
import com.example.model.Client;
import com.example.service.FirebaseService;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CompletableFuture;

public class RegisterWindow extends JFrame {
    
    // Client fields
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    
    // Address fields
    private JTextField streetField;
    private JTextField numberField;
    private JTextField complementField;
    private JTextField cityField;
    private JTextField stateField;
    private JTextField zipCodeField;
    
    public RegisterWindow() {
        setTitle("Register Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Register New Client", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form Panel with ScrollPane
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Client Information Section
        addSectionTitle(formPanel, gbc, row++, "Client Information");
        
        nameField = addFormField(formPanel, gbc, row++, "Name:");
        emailField = addFormField(formPanel, gbc, row++, "Email:");
        phoneField = addFormField(formPanel, gbc, row++, "Phone:");
        
        // Address Information Section
        addSectionTitle(formPanel, gbc, row++, "Address Information");
        
        streetField = addFormField(formPanel, gbc, row++, "Street:");
        numberField = addFormField(formPanel, gbc, row++, "Number:");
        complementField = addFormField(formPanel, gbc, row++, "Complement:");
        cityField = addFormField(formPanel, gbc, row++, "City:");
        stateField = addFormField(formPanel, gbc, row++, "State:");
        zipCodeField = addFormField(formPanel, gbc, row++, "Zip Code:");
        
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton saveButton = new JButton("Save");
        JButton clearButton = new JButton("Clear");
        JButton backButton = new JButton("Back");
        
        saveButton.addActionListener(e -> saveClient());
        clearButton.addActionListener(e -> clearForm());
        backButton.addActionListener(e -> goBack());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void addSectionTitle(JPanel panel, GridBagConstraints gbc, int row, String title) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(sectionLabel, gbc);
        gbc.gridwidth = 1;
    }
    
    private JTextField addFormField(JPanel panel, GridBagConstraints gbc, int row, String labelText) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        panel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField textField = new JTextField(20);
        panel.add(textField, gbc);
        
        return textField;
    }
    
    private void saveClient() {
        // Validate required fields
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create Address
        Address address = new Address();
        address.setStreet(streetField.getText());
        address.setNumber(numberField.getText());
        address.setComplement(complementField.getText());
        address.setCity(cityField.getText());
        address.setState(stateField.getText());
        address.setZipCode(zipCodeField.getText());
        
        // Create Client with auto-generated ID
        Client client = new Client();
        client.setId(java.util.UUID.randomUUID().toString());
        client.setName(nameField.getText());
        client.setEmail(emailField.getText());
        client.setPhone(phoneField.getText());
        client.setAddress(address);
        
        // Save to local storage first
        com.example.storage.ClientStorage.addClient(client);
        
        // Save to Firebase in background thread
        CompletableFuture.runAsync(() -> {
            try {
                FirebaseService.getInstance().saveClient(client).get();
                
                // Show success message on UI thread
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                        "Client saved successfully to Firebase!\n\nName: " + client.getName(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                });
                
            } catch (Exception e) {
                // Show error message on UI thread
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                        "Saved locally but failed to sync with Firebase:\n" + e.getMessage(),
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                });
                e.printStackTrace();
            }
        });
    }
    
    private void clearForm() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        streetField.setText("");
        numberField.setText("");
        complementField.setText("");
        cityField.setText("");
        stateField.setText("");
        zipCodeField.setText("");
    }
    
    private void goBack() {
        MainWindow mainWindow = new MainWindow();
        mainWindow.setVisible(true);
        this.dispose();
    }
}
