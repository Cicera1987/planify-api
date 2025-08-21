package com.tcc.planify_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "package")
public class PackageEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 255)
  private String name;

  @Column(name = "total_price", nullable = false)
  private Double totalPrice;

  @Column(name = "number_sessions", nullable = false)
  private Integer numberSessions;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id", nullable = false)
  private UserEntity owner;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "packageEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PackageServiceEntity> services = new ArrayList<>();

  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDateTime.now();
  }
}