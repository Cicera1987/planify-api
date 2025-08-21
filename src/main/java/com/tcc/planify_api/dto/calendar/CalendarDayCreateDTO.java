package com.tcc.planify_api.dto.calendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDayCreateDTO {
  private LocalDate localDate;
  private List<CalendarTimeCreateDTO> times;
}
