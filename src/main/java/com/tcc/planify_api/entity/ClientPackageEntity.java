package com.tcc.planify_api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client_package")
public class ClientPackageEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Pacote vinculado ao cliente
  @ManyToOne
  @JoinColumn(name = "package_id", nullable = false)
  private PackageEntity packageEntity;

  // Contato/cliente que recebeu o pacote
  @ManyToOne
  @JoinColumn(name = "contact_id", nullable = false)
  private ContactEntity contact;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  // Lista de serviços do pacote para este cliente
  @OneToMany(mappedBy = "clientPackage", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ClientPackageServiceEntity> services = new ArrayList<>();

  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDateTime.now();
  }
}