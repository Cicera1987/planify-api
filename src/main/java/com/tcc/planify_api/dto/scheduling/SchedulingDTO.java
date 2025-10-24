package com.tcc.planify_api.dto.scheduling;

import com.tcc.planify_api.dto.calendar.CalendarDayDTO;
import com.tcc.planify_api.dto.calendar.CalendarTimeDTO;
import com.tcc.planify_api.dto.contact.ContactDTO;
import com.tcc.planify_api.dto.packageServices.PackageDTO;
import com.tcc.planify_api.dto.typeOfService.TypeOfServiceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchedulingDTO {
  private Long id;

  private ContactDTO contact;

  private List<TypeOfServiceDTO> services;
  private PackageDTO packageInfo;

  private CalendarDayDTO calendarDay;
  private CalendarTimeDTO calendarTime;
  private String status;
  private LocalDateTime createdAt;
}