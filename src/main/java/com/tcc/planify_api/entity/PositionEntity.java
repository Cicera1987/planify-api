package com.tcc.planify_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcc.planify_api.enums.PositionEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import java.util.Set;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity(name = "position")
@Table(name = "position")
public class PositionEntity implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, unique = true)
  @Enumerated(EnumType.STRING)
  private PositionEnum position;

  @JsonIgnore
  @OneToMany(mappedBy = "position")
  private Set<UserEntity> users;

  @Override
  public String getAuthority() {
    return position.name();
  }
}
