package com.example.gui;

import com.example.model.Address;
import com.example.model.Client;

import com.example.service.FirebaseService;
import com.example.service.ViaCepService;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CompletableFuture;

public class RegisterWindow extends JFrame {
    
    // Client fields
    private JTextField nameField;
    private JTextField emailField;
    private JTextField cpfField;
    
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
        
        addSectionTitle(formPanel, gbc, row++, "Client Information");
        
        nameField = addFormField(formPanel, gbc, row++, "Name:");
        emailField = addFormField(formPanel, gbc, row++, "Email:");
        cpfField = addFormField(formPanel, gbc, row++, "CPF (11 digits):");
        
        addSectionTitle(formPanel, gbc, row++, "Address Information");
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel zipLabel = new JLabel("Zip Code (CEP):");
        formPanel.add(zipLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        zipCodeField = new JTextField(20);
        formPanel.add(zipCodeField, gbc);
        
        gbc.gridx = 2;
        gbc.weightx = 0.2;
        JButton searchCepButton = new JButton("Search CEP");
        searchCepButton.addActionListener(e -> searchCep());
        formPanel.add(searchCepButton, gbc);
        row++;
        
        streetField = addFormField(formPanel, gbc, row++, "Street:");
        numberField = addFormField(formPanel, gbc, row++, "Number:");
        complementField = addFormField(formPanel, gbc, row++, "Complement (optional):");
        cityField = addFormField(formPanel, gbc, row++, "City:");
        stateField = addFormField(formPanel, gbc, row++, "State:");
        
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
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return;
        }
        
        if (emailField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return;
        }
        
        String cpf = cpfField.getText().trim();
        if (!cpf.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(this, 
                "CPF must contain exactly 11 digits (numbers only)!", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            cpfField.requestFocus();
            return;
        }
        
        if (zipCodeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Zip Code is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            zipCodeField.requestFocus();
            return;
        }
        
        if (streetField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Street is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            streetField.requestFocus();
            return;
        }
        
        if (numberField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Number is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            numberField.requestFocus();
            return;
        }
        
        if (cityField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "City is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            cityField.requestFocus();
            return;
        }
        
        if (stateField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "State is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            stateField.requestFocus();
            return;
        }
        
        Address address = new Address();
        address.setStreet(streetField.getText().trim());
        address.setNumber(numberField.getText().trim());
        address.setComplement(complementField.getText().trim());
        address.setCity(cityField.getText().trim());
        address.setState(stateField.getText().trim());
        address.setZipCode(zipCodeField.getText().trim());
        
        Client client = new Client();
        client.setId(java.util.UUID.randomUUID().toString());
        client.setName(nameField.getText());
        client.setEmail(emailField.getText());
        client.setCpf(cpf);
        client.setAddress(address);
        
        com.example.storage.ClientStorage.addClient(client);
        
        CompletableFuture.runAsync(() -> {
            try {
                FirebaseService.getInstance().saveClient(client).get();
                
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                        "Client saved successfully to Firebase!\n\nName: " + client.getName(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                });
                
            } catch (Exception e) {
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
        cpfField.setText("");
        streetField.setText("");
        numberField.setText("");
        complementField.setText("");
        cityField.setText("");
        stateField.setText("");
        zipCodeField.setText("");
    }
    
    private void searchCep() {
        String cep = zipCodeField.getText().trim();
        
        if (cep.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a CEP", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Component[] components = ((JPanel)zipCodeField.getParent()).getComponents();
        JButton searchButton = null;
        for (Component comp : components) {
            if (comp instanceof JButton && ((JButton)comp).getText().equals("Search CEP")) {
                searchButton = (JButton) comp;
                break;
            }
        }
        
        if (searchButton != null) {
            searchButton.setEnabled(false);
            searchButton.setText("Searching...");
        }
        
        final JButton finalSearchButton = searchButton;
        
        ViaCepService.getInstance()
            .getAddressByCep(cep)
            .thenAccept(response -> {
                SwingUtilities.invokeLater(() -> {
                    streetField.setText(response.getLogradouro() != null ? response.getLogradouro() : "");
                    cityField.setText(response.getLocalidade() != null ? response.getLocalidade() : "");
                    stateField.setText(response.getUf() != null ? response.getUf() : "");
                    
                    String formattedCep = ViaCepService.getInstance().formatCep(response.getCep());
                    zipCodeField.setText(formattedCep);
                    
                    Toast.show(this, "Address loaded successfully!", 2000);
                    
                    if (finalSearchButton != null) {
                        finalSearchButton.setEnabled(true);
                        finalSearchButton.setText("Search CEP");
                    }
                    
                    numberField.requestFocus();
                });
            })
            .exceptionally(e -> {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                        "Failed to fetch address:\n" + e.getMessage(),
                        "CEP Error",
                        JOptionPane.ERROR_MESSAGE);
                    
                    if (finalSearchButton != null) {
                        finalSearchButton.setEnabled(true);
                        finalSearchButton.setText("Search CEP");
                    }
                });
                return null;
            });
    }
    
    private void goBack() {
        MainWindow mainWindow = new MainWindow();
        mainWindow.setVisible(true);
        this.dispose();
    }
}
