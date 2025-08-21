package com.tcc.planify_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "calendar_day")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDayEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "local_date", nullable = false)
  private LocalDate localDate;

  @OneToMany(mappedBy = "calendarDay", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CalendarTimeEntity> times;

  @Column(name = "created_at", nullable = false, updatable = false)
  private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
}
