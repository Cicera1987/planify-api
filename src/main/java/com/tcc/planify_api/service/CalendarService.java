package com.tcc.planify_api.service;

import com.tcc.planify_api.dto.calendar.CalendarDayCreateDTO;
import com.tcc.planify_api.dto.calendar.CalendarDayDTO;
import com.tcc.planify_api.dto.calendar.CalendarTimeCreateDTO;
import com.tcc.planify_api.dto.calendar.CalendarTimeDTO;
import com.tcc.planify_api.entity.CalendarDayEntity;
import com.tcc.planify_api.entity.CalendarTimeEntity;
import com.tcc.planify_api.repository.CalendarDayRepository;
import com.tcc.planify_api.repository.CalendarTimeRepository;
import com.tcc.planify_api.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarService {

  private final CalendarDayRepository dayRepository;
  private final CalendarTimeRepository timeRepository;

  @Transactional
  public List<CalendarDayDTO> createDay(List<CalendarDayCreateDTO> dayDTOs) {
    Long userId = AuthUtil.getAuthenticatedProfessionalId();

    List<CalendarDayEntity> days = dayDTOs.stream().map(dto -> {
      CalendarDayEntity day = new CalendarDayEntity();
      day.setUserId(userId);
      day.setLocalDate(dto.getLocalDate());

      List<CalendarTimeEntity> times = dto.getTimes().stream().map(timeDTO -> {
        CalendarTimeEntity time = new CalendarTimeEntity();
        time.setTime(timeDTO.getTime());
        time.setCalendarDay(day);
        return time;
      }).collect(Collectors.toList());

      day.setTimes(times);
      return day;
    }).collect(Collectors.toList());

    List<CalendarDayEntity> savedDays = dayRepository.saveAll(days);
    return savedDays.stream().map(this::mapToDTO).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<CalendarDayDTO> listDays() {
    return dayRepository.findAll().stream()
          .map(this::mapToDTO)
          .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<CalendarDayDTO> getCalendarDaysByUser(Long userId) {
    return dayRepository.findAll().stream()
          .filter(day -> day.getUserId().equals(userId))
          .map(this::mapToDTO)
          .collect(Collectors.toList());
  }

  @Transactional
  public CalendarDayDTO updateDay(Long idDay, CalendarDayCreateDTO dto) throws Exception {
    CalendarDayEntity day = dayRepository.findById(idDay)
          .orElseThrow(() -> new Exception("Dia não encontrado"));

    day.setLocalDate(dto.getLocalDate());

    day.getTimes().clear();
    List<CalendarTimeEntity> times = dto.getTimes().stream().map(timeDTO -> {
      CalendarTimeEntity time = new CalendarTimeEntity();
      time.setTime(timeDTO.getTime());
      time.setCalendarDay(day);
      return time;
    }).toList();

    day.getTimes().addAll(times);

    CalendarDayEntity updatedDay = dayRepository.save(day);
    return mapToDTO(updatedDay);
  }

  @Transactional
  public void deleteDay(Long idDay) throws Exception {
    CalendarDayEntity day = dayRepository.findById(idDay)
          .orElseThrow(() -> new Exception("Dia não encontrado"));
    dayRepository.delete(day);
  }

  @Transactional
  public CalendarTimeDTO addTime(Long idDay, CalendarTimeCreateDTO dto) {
    CalendarDayEntity day = dayRepository.findById(idDay)
          .orElseThrow(() -> new RuntimeException("Dia não encontrado"));

    CalendarTimeEntity time = new CalendarTimeEntity();
    time.setTime(dto.getTime());
    time.setCalendarDay(day);

    CalendarTimeEntity savedTime = timeRepository.save(time);
    return mapTimeToDTO(savedTime);
  }

  @Transactional
  public void deleteTime(Long idDay, Long idTime) throws Exception {
    CalendarDayEntity day = dayRepository.findById(idDay)
          .orElseThrow(() -> new Exception("Dia não encontrado"));

    CalendarTimeEntity time = day.getTimes().stream()
          .filter(t -> t.getId().equals(idTime))
          .findFirst()
          .orElseThrow(() -> new Exception("Horário não encontrado"));

    timeRepository.delete(time);
  }

  private CalendarDayDTO mapToDTO(CalendarDayEntity day) {
    CalendarDayDTO dto = new CalendarDayDTO();
    dto.setId(day.getId());
    dto.setUserId(day.getUserId());
    dto.setLocalDate(day.getLocalDate());
    dto.setTimes(day.getTimes().stream()
          .map(this::mapTimeToDTO)
          .collect(Collectors.toList()));
    return dto;
  }

  private CalendarTimeDTO mapTimeToDTO(CalendarTimeEntity time) {
    CalendarTimeDTO dto = new CalendarTimeDTO();
    dto.setId(time.getId());
    dto.setTime(time.getTime());
    return dto;
  }
}