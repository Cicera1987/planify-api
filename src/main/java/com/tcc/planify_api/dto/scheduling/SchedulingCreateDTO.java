package com.tcc.planify_api.dto.scheduling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchedulingCreateDTO {
  private Long contactId;
  private List<Long> serviceId;
  private Long packageId;
  private Long calendarDayId;
  private Long calendarTimeId;
  private Long clientPackageId;
}
