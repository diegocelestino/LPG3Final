package com.example.gui;

import com.example.model.Client;
import com.example.storage.ClientStorage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DataWindow extends JFrame {
    
    private JTable clientTable;
    private DefaultTableModel tableModel;
    
    public DataWindow() {
        setTitle("Client Data");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        
        initComponents();
        loadClients();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Client Data", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"ID", "Name", "Email", "Phone", "Street", "Number", "City", "State", "Zip Code"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        clientTable = new JTable(tableModel);
        clientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clientTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(clientTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton refreshButton = new JButton("Refresh");
        JButton deleteButton = new JButton("Delete Selected");
        JButton backButton = new JButton("Back to Main");
        
        refreshButton.addActionListener(e -> loadClients());
        deleteButton.addActionListener(e -> deleteSelectedClient());
        backButton.addActionListener(e -> goBack());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadClients() {
        tableModel.setRowCount(0);
        
        List<Client> clients = ClientStorage.getAllClients();
        
        for (Client client : clients) {
            Object[] row = {
                client.getId(),
                client.getName(),
                client.getEmail(),
                client.getPhone(),
                client.getAddress() != null ? client.getAddress().getStreet() : "",
                client.getAddress() != null ? client.getAddress().getNumber() : "",
                client.getAddress() != null ? client.getAddress().getCity() : "",
                client.getAddress() != null ? client.getAddress().getState() : "",
                client.getAddress() != null ? client.getAddress().getZipCode() : ""
            };
            tableModel.addRow(row);
        }
    }
    
    private void deleteSelectedClient() {
        int selectedRow = clientTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a client to delete.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String clientId = (String) tableModel.getValueAt(selectedRow, 0);
        String clientName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete client: " + clientName + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            ClientStorage.removeClient(clientId);
            loadClients();
            JOptionPane.showMessageDialog(this,
                "Client deleted successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void goBack() {
        MainWindow mainWindow = new MainWindow();
        mainWindow.setVisible(true);
        this.dispose();
    }
}
