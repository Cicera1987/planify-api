package com.tcc.planify_api.dto.calendar;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CalendarTimeDTO {
  private Long id;
  private LocalTime time;
}
