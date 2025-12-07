package com.tcc.planify_api.dto.calendar;

import lombok.*;

import java.time.LocalTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarTimeDTO {
  private Long id;
  private LocalTime time;
  private boolean available;
}