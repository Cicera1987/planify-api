package com.tcc.planify_api.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

  @Value("${firebase.project_id}")
  private String projectId;

  @Value("${firebase.private_key_id}")
  private String privateKeyId;

  @Value("${firebase.private_key}")
  private String privateKey;

  @Value("${firebase.client_email}")
  private String clientEmail;

  @Value("${firebase.client_id}")
  private String clientId;

  @PostConstruct
  public void initFirebase() {
    try {
      privateKey = privateKey.replace("\\n", "\n");

      String json = "{\n" +
            "  \"type\": \"service_account\",\n" +
            "  \"project_id\": \"" + projectId + "\",\n" +
            "  \"private_key_id\": \"" + privateKeyId + "\",\n" +
            "  \"private_key\": \"" + privateKey + "\",\n" +
            "  \"client_email\": \"" + clientEmail + "\",\n" +
            "  \"client_id\": \"" + clientId + "\",\n" +
            "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
            "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
            "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
            "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/" + clientEmail + "\"\n" +
            "}";

      InputStream firebaseStream = new ByteArrayInputStream(json.getBytes());

      FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(firebaseStream))
            .setProjectId(projectId)
            .build();

      if (FirebaseApp.getApps().isEmpty()) {
        FirebaseApp.initializeApp(options);
      }

    } catch (Exception e) {
      throw new RuntimeException("Erro ao inicializar Firebase: " + e.getMessage(), e);
    }
  }
}
