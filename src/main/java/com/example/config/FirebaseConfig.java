package com.example.config;

import com.example.service.FirebaseService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FirebaseConfig {
    private static boolean initialized = false;

    public static void initialize() {
        if (initialized) {
            return;
        }

        try {
            Properties props = new Properties();
            InputStream input = FirebaseConfig.class.getClassLoader()
                    .getResourceAsStream("firebase-config.properties");

            if (input == null) {
                System.err.println("Unable to find firebase-config.properties");
                return;
            }

            props.load(input);

            String serviceAccountPath = props.getProperty("firebase.serviceAccountPath");
            String databaseUrl = props.getProperty("firebase.databaseUrl");

            FirebaseService.getInstance().initialize(serviceAccountPath, databaseUrl);
            initialized = true;
            System.out.println("Firebase initialized successfully!");

        } catch (IOException e) {
            System.err.println("Error initializing Firebase: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
