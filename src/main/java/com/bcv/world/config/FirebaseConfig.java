package com.bcv.world.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            // Read the Firebase credentials JSON from environment variable
            String json = System.getenv("FIREBASE_CONFIG_BASE64");

            if (json == null || json.isEmpty()) {
                throw new IllegalStateException("Missing FIREBASE_CREDENTIALS_JSON environment variable");
            }

            // Convert the JSON string into an InputStream
            InputStream serviceAccount = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

            // Build FirebaseOptions with credentials
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            // Initialize Firebase app only if not already initialized
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase has been initialized successfully.");
            } else {
                System.out.println("Firebase app already initialized.");
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }
}
