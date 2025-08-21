package com.tcc.planify_api.dto.calendar;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CalendarDayDTO {
  private Long id;
  private Long userId;
  private LocalDate localDate;
  private List<CalendarTimeDTO> times;
}
