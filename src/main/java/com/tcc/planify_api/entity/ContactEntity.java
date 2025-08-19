package com.tcc.planify_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
}
