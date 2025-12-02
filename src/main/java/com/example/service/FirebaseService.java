package com.example.service;

import com.example.model.Client;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class FirebaseService {
    private static FirebaseService instance;
    private DatabaseReference databaseRef;
    private Gson gson;
    private boolean initialized = false;

    private FirebaseService() {
        gson = new Gson();
    }

    public static FirebaseService getInstance() {
        if (instance == null) {
            instance = new FirebaseService();
        }
        return instance;
    }

    public void initialize(String serviceAccountPath, String databaseUrl) throws IOException {
        if (initialized) {
            return;
        }

        FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(databaseUrl)
                .build();

        FirebaseApp.initializeApp(options);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        initialized = true;
    }

    public CompletableFuture<Void> saveClient(Client client) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        databaseRef.child("clients").child(client.getId()).setValue(client, (error, ref) -> {
            if (error != null) {
                future.completeExceptionally(new Exception("Failed to save client: " + error.getMessage()));
            } else {
                future.complete(null);
            }
        });

        return future;
    }

    public CompletableFuture<List<Client>> getAllClients() {
        CompletableFuture<List<Client>> future = new CompletableFuture<>();

        databaseRef.child("clients").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Client> clients = new ArrayList<>();
                for (DataSnapshot clientSnapshot : snapshot.getChildren()) {
                    Client client = clientSnapshot.getValue(Client.class);
                    if (client != null) {
                        clients.add(client);
                    }
                }
                future.complete(clients);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new Exception("Failed to fetch clients: " + error.getMessage()));
            }
        });

        return future;
    }

    public CompletableFuture<Void> deleteClient(String clientId) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        databaseRef.child("clients").child(clientId).removeValue((error, ref) -> {
            if (error != null) {
                future.completeExceptionally(new Exception("Failed to delete client: " + error.getMessage()));
            } else {
                future.complete(null);
            }
        });

        return future;
    }

    public CompletableFuture<Client> getClientById(String clientId) {
        CompletableFuture<Client> future = new CompletableFuture<>();

        databaseRef.child("clients").child(clientId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Client client = snapshot.getValue(Client.class);
                future.complete(client);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new Exception("Failed to fetch client: " + error.getMessage()));
            }
        });

        return future;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
