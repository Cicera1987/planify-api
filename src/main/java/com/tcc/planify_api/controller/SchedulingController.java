package com.tcc.planify_api.controller;

import com.tcc.planify_api.docs.SchedulingApi;
import com.tcc.planify_api.dto.pagination.PageDTO;
import com.tcc.planify_api.dto.scheduling.SchedulingCreateDTO;
import com.tcc.planify_api.dto.scheduling.SchedulingDTO;
import com.tcc.planify_api.enums.StatusAgendamento;
import com.tcc.planify_api.service.SchedulingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SchedulingController implements SchedulingApi {

  private final SchedulingService schedulingService;

  @Override
  public ResponseEntity<PageDTO<SchedulingDTO>> getActiveSchedulings(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size);
    PageDTO<SchedulingDTO> schedulings = schedulingService.getActiveSchedulingsForProfessional(pageable);
    return ResponseEntity.ok(schedulings);
  }

  @Override
  public ResponseEntity<PageDTO<SchedulingDTO>> getSchedulingHistory(
        LocalDate startDate,
        LocalDate endDate,
        List<String> statuses,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size);
    PageDTO<SchedulingDTO> schedulings = schedulingService.getSchedulingHistory(startDate, endDate, statuses, pageable);
    return ResponseEntity.ok(schedulings);
  }

  @Override
  public ResponseEntity<SchedulingDTO> createScheduling(@Valid @RequestBody SchedulingCreateDTO schedulingCreateDTO) {
    SchedulingDTO dto = schedulingService.createScheduling(schedulingCreateDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @Override
  public ResponseEntity<SchedulingDTO> updateStatus(Long idScheduling, StatusAgendamento newStatus) {
    SchedulingDTO updated = schedulingService.updateStatus(idScheduling, newStatus);
    return ResponseEntity.ok(updated);
  }

  @Override
  public ResponseEntity<Void> deleteScheduling(Long idScheduling) {
    schedulingService.deleteScheduling(idScheduling);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<PageDTO<SchedulingDTO>> searchSchedulingsByContactName(
        @RequestParam String name,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size);
    PageDTO<SchedulingDTO> schedulingsPage = schedulingService.searchSchedulingsByContactName(name, pageable);
    return ResponseEntity.ok(schedulingsPage);
  }
}