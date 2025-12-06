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
@Table(name = "scheduling")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchedulingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "professional_id", nullable = false)
  private UserEntity professional;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "contact_id", nullable = false)
  private ContactEntity contact;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "service_id")
  private TypeOfServiceEntity service;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "package_id")
  private PackageEntity packageEntity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "calendar_time_id", nullable = false)
  private CalendarTimeEntity calendarTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "calendar_day_id", nullable = false)
  private CalendarDayEntity calendarDay;

  @Column(nullable = false)
  private String status;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @ManyToMany
  @JoinTable(
        name = "scheduling_services",
        joinColumns = @JoinColumn(name = "scheduling_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
  )
  private List<TypeOfServiceEntity> services = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "client_package_id")
  private ClientPackageEntity clientPackage;

  public void addService(TypeOfServiceEntity service) {
    if (this.services == null) {
      this.services = new ArrayList<>();
    }
    this.services.add(service);
  }
}
