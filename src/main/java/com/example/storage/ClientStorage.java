package com.example.storage;

import com.example.model.Address;
import com.example.model.Client;
import java.util.ArrayList;
import java.util.List;

public class ClientStorage {
    private static final List<Client> clients = new ArrayList<>();
    
    static {
        // Initialize with mock data
        Address address1 = new Address();
        address1.setStreet("Main Street");
        address1.setNumber("123");
        address1.setComplement("Apt 4B");
        address1.setCity("New York");
        address1.setState("NY");
        address1.setZipCode("10001");
        
        Client client1 = new Client();
        client1.setId("1");
        client1.setName("John Doe");
        client1.setEmail("john.doe@example.com");
        client1.setPhone("(555) 123-4567");
        client1.setAddress(address1);
        
        Address address2 = new Address();
        address2.setStreet("Oak Avenue");
        address2.setNumber("456");
        address2.setCity("Los Angeles");
        address2.setState("CA");
        address2.setZipCode("90001");
        
        Client client2 = new Client();
        client2.setId("2");
        client2.setName("Jane Smith");
        client2.setEmail("jane.smith@example.com");
        client2.setPhone("(555) 987-6543");
        client2.setAddress(address2);
        
        Address address3 = new Address();
        address3.setStreet("Elm Street");
        address3.setNumber("789");
        address3.setComplement("Suite 200");
        address3.setCity("Chicago");
        address3.setState("IL");
        address3.setZipCode("60601");
        
        Client client3 = new Client();
        client3.setId("3");
        client3.setName("Bob Johnson");
        client3.setEmail("bob.johnson@example.com");
        client3.setPhone("(555) 555-5555");
        client3.setAddress(address3);
        
        clients.add(client1);
        clients.add(client2);
        clients.add(client3);
    }
    
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
}
