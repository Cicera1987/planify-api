package com.tcc.planify_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contact")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String phone;

  @Column
  private String email;

  @Column
  private String observation;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "professional_id", nullable = false)
  private UserEntity professional;

  @Column(name = "image_url")
  private String imageUrl;

  @ManyToMany
  @JoinTable(
        name = "contact_packages",
        joinColumns = @JoinColumn(name = "contact_id"),
        inverseJoinColumns = @JoinColumn(name = "package_id")
  )
  private List<PackageEntity> packages = new ArrayList<>();


  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
}
