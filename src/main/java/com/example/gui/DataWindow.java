package com.example.gui;

import com.example.model.Client;
import com.example.service.FirebaseService;
import com.example.storage.ClientStorage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DataWindow extends JFrame {
    
    private JTable clientTable;
    private DefaultTableModel tableModel;
    private Thread autoRefreshThread;
    private volatile boolean isRunning = true;
    
    public DataWindow() {
        setTitle("Client Data");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        
        initComponents();
        loadClients();
        startAutoRefresh();
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                stopAutoRefresh();
            }
        });
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Client Data", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"ID", "Name", "Email", "CPF", "Street", "Number", "City", "State", "Zip Code"};
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
        
        refreshButton.addActionListener(e -> {
            loadClients();
            Toast.show(this, "Data refreshed manually", 1500);
        });
        deleteButton.addActionListener(e -> deleteSelectedClient());
        backButton.addActionListener(e -> goBack());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadClients() {
        CompletableFuture.runAsync(() -> {
            try {
                List<Client> clients = FirebaseService.getInstance().getAllClients().get();
                
                ClientStorage.setClients(clients);
                
                SwingUtilities.invokeLater(() -> {
                    tableModel.setRowCount(0);
                    
                    for (Client client : clients) {
                        Object[] row = {
                            client.getId(),
                            client.getName(),
                            client.getEmail(),
                            client.getCpf(),
                            client.getAddress() != null ? client.getAddress().getStreet() : "",
                            client.getAddress() != null ? client.getAddress().getNumber() : "",
                            client.getAddress() != null ? client.getAddress().getCity() : "",
                            client.getAddress() != null ? client.getAddress().getState() : "",
                            client.getAddress() != null ? client.getAddress().getZipCode() : ""
                        };
                        tableModel.addRow(row);
                    }
                });
                
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                        "Failed to load clients from Firebase:\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                });
                e.printStackTrace();
            }
        });
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
            CompletableFuture.runAsync(() -> {
                try {
                    FirebaseService.getInstance().deleteClient(clientId).get();
                    
                    ClientStorage.removeClient(clientId);
                    
                    SwingUtilities.invokeLater(() -> {
                        loadClients();
                        Toast.show(this, "Client deleted successfully!", 2000);
                    });
                    
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this,
                            "Failed to delete client from Firebase:\n" + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    });
                    e.printStackTrace();
                }
            });
        }
    }
    
    private void startAutoRefresh() {
        autoRefreshThread = new Thread(() -> {
            while (isRunning) {
                try {
                    Thread.sleep(10000);
                    
                    if (isRunning) {
                        SwingUtilities.invokeLater(() -> {
                            loadClients();
                            Toast.show(this, "Data refreshed - " + tableModel.getRowCount() + " clients", 2000);
                            System.out.println("Auto-refreshed client data at: " + 
                                new java.util.Date());
                        });
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        
        autoRefreshThread.setDaemon(true);
        autoRefreshThread.setName("DataWindow-AutoRefresh");
        autoRefreshThread.start();
        System.out.println("Auto-refresh thread started - refreshing every 10 seconds");
    }
    
    private void stopAutoRefresh() {
        isRunning = false;
        if (autoRefreshThread != null && autoRefreshThread.isAlive()) {
            autoRefreshThread.interrupt();
        }
        System.out.println("Auto-refresh thread stopped");
    }
    
    private void goBack() {
        stopAutoRefresh();
        MainWindow mainWindow = new MainWindow();
        mainWindow.setVisible(true);
        this.dispose();
    }
}
