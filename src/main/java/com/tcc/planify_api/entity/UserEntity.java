package com.tcc.planify_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class UserEntity implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  @Email(message = "Email inválido")
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

  @Column(name = "image_url")
  private String imageUrl;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }
  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}