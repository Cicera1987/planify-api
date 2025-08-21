package com.tcc.planify_api.dto.calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarTimeCreateDTO {
  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  private LocalTime time;
}
