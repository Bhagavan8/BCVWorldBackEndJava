package com.bcv.world.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            // Read the entire JSON from environment variable
            String firebaseCredentialsJson = System.getenv("FIREBASE_CREDENTIALS_JSON");
            if (firebaseCredentialsJson == null || firebaseCredentialsJson.isEmpty()) {
                throw new IllegalStateException("Missing FIREBASE_CREDENTIALS_JSON environment variable");
            }

            // Convert String to InputStream
            InputStream serviceAccount = new ByteArrayInputStream(firebaseCredentialsJson.getBytes(StandardCharsets.UTF_8));

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    // optionally set database URL here
                    .setDatabaseUrl("https://your-project-id.firebaseio.com")
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase initialized successfully");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Firebase initialization failed", e);
        }
    }
}
