package com.tcc.planify_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client_package_service")
public class ClientPackageServiceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "client_package_id", nullable = false)
  private ClientPackageEntity clientPackage;

  @ManyToOne
  @JoinColumn(name = "service_id", nullable = false)
  private TypeOfServiceEntity service;

  @Column(nullable = false)
  private Integer quantity;
}