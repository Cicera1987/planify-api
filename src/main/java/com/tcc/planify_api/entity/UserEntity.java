package com.tcc.planify_api.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "speciality", nullable = false)
  private String speciality;

  @Column(name = "phone", nullable = false)
  private String phone;

  @Column(name = "active", nullable = false)
  private boolean active;

  @ManyToOne
  @JoinColumn(name = "position_id", nullable = false)
  private PositionEntity position;

}