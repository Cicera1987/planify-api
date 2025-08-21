package com.tcc.planify_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "packages_services")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageServiceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "package_id")
  private PackageEntity packageEntity;

  @ManyToOne
  @JoinColumn(name = "service_id")
  private TypeOfServiceEntity service;

  @Column(nullable = false)
  private Integer quantity;
}

