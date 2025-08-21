package com.tcc.planify_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "calendar_time")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "time", nullable = false)
  private LocalTime time;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "calendar_day_id", nullable = false)
  private CalendarDayEntity calendarDay;

  @Column(name = "created_at", nullable = false, updatable = false)
  private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
}
