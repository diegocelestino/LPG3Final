package com.example.storage;

import com.example.model.Client;
import java.util.ArrayList;
import java.util.List;

public class ClientStorage {
    private static final List<Client> clients = new ArrayList<>();
    
    public static void addClient(Client client) {
        clients.add(client);
    }
    
    public static List<Client> getAllClients() {
        return new ArrayList<>(clients);
    }
    
    public static void removeClient(String id) {
        clients.removeIf(client -> client.getId().equals(id));
    }
    
    public static Client getClientById(String id) {
        return clients.stream()
                .filter(client -> client.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    public static int getClientCount() {
        return clients.size();
    }
    
    public static void setClients(List<Client> newClients) {
        clients.clear();
        clients.addAll(newClients);
    }
}
