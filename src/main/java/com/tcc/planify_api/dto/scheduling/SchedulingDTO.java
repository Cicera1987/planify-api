package com.tcc.planify_api.dto.scheduling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchedulingDTO {
  private Long id;
  private Long contactId;
  private List<Long> serviceId;
  private Long packageId;
  private Long calendarTimeId;
  private String status;
  private LocalDateTime createdAt;
}
