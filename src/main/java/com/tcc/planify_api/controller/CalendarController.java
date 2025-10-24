package com.tcc.planify_api.controller;

import com.tcc.planify_api.docs.CalendarApi;
import com.tcc.planify_api.dto.calendar.CalendarDayCreateDTO;
import com.tcc.planify_api.dto.calendar.CalendarDayDTO;
import com.tcc.planify_api.dto.calendar.CalendarTimeCreateDTO;
import com.tcc.planify_api.dto.calendar.CalendarTimeDTO;
import com.tcc.planify_api.service.CalendarService;
import com.tcc.planify_api.util.AuthUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CalendarController implements CalendarApi {

  private final CalendarService calendarService;

  @Override
  public ResponseEntity<List<CalendarDayDTO>> listDays() {
    Long userId = AuthUtil.getAuthenticatedUserId();
    return ResponseEntity.ok(calendarService.listDays(userId));
  }

  @Override
  public ResponseEntity<List<CalendarDayDTO>> createDay(
        @Valid @RequestBody List<CalendarDayCreateDTO> calendarDayCreateDTOs) {
    List<CalendarDayDTO> createdDays = calendarService.createDay(calendarDayCreateDTOs);
    return ResponseEntity.status(201).body(createdDays);
  }


  @Override
  public ResponseEntity<CalendarDayDTO> updateDay(@NotNull Long idDay, @Valid CalendarDayCreateDTO calendarDayCreateDTO) throws Exception {
    CalendarDayDTO updatedDay = calendarService.updateDay(idDay, calendarDayCreateDTO);
    return ResponseEntity.ok(updatedDay);
  }

  @Override
  public ResponseEntity<Void> deleteDay(@NotNull Long idDay) throws Exception {
    calendarService.deleteDay(idDay);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<CalendarTimeDTO> addTime(@NotNull Long idDay, @Valid CalendarTimeCreateDTO calendarTimeCreateDTO) {
    CalendarTimeDTO createdTime = calendarService.addTime(idDay, calendarTimeCreateDTO);
    return ResponseEntity.status(201).body(createdTime);
  }

  @Override
  public ResponseEntity<Void> deleteTime(@NotNull Long idDay, @NotNull Long idTime) throws Exception {
    calendarService.deleteTime(idDay, idTime);
    return ResponseEntity.noContent().build();
  }
}