package com.tcc.planify_api.dto.scheduling;

import com.tcc.planify_api.dto.calendar.CalendarDayDTO;
import com.tcc.planify_api.dto.calendar.CalendarTimeDTO;
import com.tcc.planify_api.dto.contact.ContactDTO;
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
  private ContactDTO contact;
  private List<Long> serviceId;
  private Long packageId;
  private CalendarDayDTO calendarDay;
  private CalendarTimeDTO calendarTime;
  private String status;
  private LocalDateTime createdAt;
}
