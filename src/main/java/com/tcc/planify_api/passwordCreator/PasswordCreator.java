package com.tcc.planify_api.passwordCreator;

import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

public class PasswordCreator {
  public static void main(String[] args) {
    Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(
          "secret",
          185000,
          256,
          Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256 // algoritmo
    );

    String password = encoder.encode("admin123");
    System.out.println("Senha codificada: " + password);

    boolean matches = encoder.matches("admin123", password);
    System.out.println("Combina? " + matches);
  }
}
