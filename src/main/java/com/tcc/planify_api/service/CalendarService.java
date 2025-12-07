package com.tcc.planify_api.service;

import com.tcc.planify_api.dto.calendar.CalendarDayCreateDTO;
import com.tcc.planify_api.dto.calendar.CalendarDayDTO;
import com.tcc.planify_api.dto.calendar.CalendarTimeCreateDTO;
import com.tcc.planify_api.dto.calendar.CalendarTimeDTO;
import com.tcc.planify_api.entity.CalendarDayEntity;
import com.tcc.planify_api.entity.CalendarTimeEntity;
import com.tcc.planify_api.repository.CalendarDayRepository;
import com.tcc.planify_api.repository.CalendarTimeRepository;
import com.tcc.planify_api.repository.SchedulingRepository;
import com.tcc.planify_api.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

  private final CalendarDayRepository dayRepository;
  private final CalendarTimeRepository timeRepository;
  private final SchedulingRepository schedulingRepository;

  @Transactional
  public List<CalendarDayDTO> createDay(List<CalendarDayCreateDTO> dayDTOs) {
    Long userId = AuthUtil.getAuthenticatedUserId();

    List<CalendarDayEntity> savedDays = dayDTOs.stream()
          .map(dto -> {
            CalendarDayEntity day = dayRepository
                  .findByUserIdAndLocalDate(userId, dto.getLocalDate())
                  .orElseGet(() -> new CalendarDayEntity(
                        null,
                        userId,
                        dto.getLocalDate(),
                        new ArrayList<>(),
                        LocalDateTime.now()
                  ));

            for (CalendarTimeCreateDTO timeDTO : dto.getTimes()) {
              boolean exists = day.getTimes().stream()
                    .anyMatch(t -> t.getTime().equals(timeDTO.getTime()));

              if (!exists) {
                CalendarTimeEntity time = new CalendarTimeEntity();
                time.setTime(timeDTO.getTime());
                time.setCalendarDay(day);
                day.getTimes().add(time);
              }
            }

            return dayRepository.save(day);
          })
          .toList();

    return savedDays.stream().map(this::mapToDTO).toList();
  }

  @Transactional(readOnly = true)
  public List<CalendarDayDTO> listDays() {
    Long userId = AuthUtil.getAuthenticatedUserId();

    List<CalendarDayEntity> days = dayRepository.findByUserId(userId);

    return days.stream().map(day -> {
      CalendarDayDTO dto = new CalendarDayDTO();
      dto.setId(day.getId());
      dto.setUserId(day.getUserId());
      dto.setLocalDate(day.getLocalDate());

      List<CalendarTimeDTO> times = day.getTimes().stream()
            .sorted(Comparator.comparing(CalendarTimeEntity::getTime))
            .map(time -> {
              boolean occupied = schedulingRepository.existsByCalendarTimeId(time.getId());
              return new CalendarTimeDTO(time.getId(), time.getTime(), !occupied);
            })
            .toList();

      dto.setTimes(times);
      return dto;
    }).toList();
  }

  @Transactional
  public CalendarDayDTO updateDay(Long idDay, CalendarDayCreateDTO dto) throws Exception {
    Long userId = AuthUtil.getAuthenticatedUserId();

    CalendarDayEntity day = dayRepository.findByIdAndUserId(idDay, userId)
          .orElseThrow(() -> new Exception("Dia não encontrado"));

    day.setLocalDate(dto.getLocalDate());

    day.getTimes().clear();
    for (CalendarTimeCreateDTO t : dto.getTimes()) {
      CalendarTimeEntity time = new CalendarTimeEntity();
      time.setTime(t.getTime());
      time.setCalendarDay(day);
      day.getTimes().add(time);
    }

    return mapToDTO(dayRepository.save(day));
  }

  @Transactional
  public void deleteDay(Long idDay) throws Exception {
    Long userId = AuthUtil.getAuthenticatedUserId();

    CalendarDayEntity day = dayRepository.findByIdAndUserId(idDay, userId)
          .orElseThrow(() -> new Exception("Dia não encontrado"));

    dayRepository.delete(day);
  }

  @Transactional
  public CalendarTimeDTO addTime(Long idDay, CalendarTimeCreateDTO dto) {
    Long userId = AuthUtil.getAuthenticatedUserId();

    CalendarDayEntity day = dayRepository.findByIdAndUserId(idDay, userId)
          .orElseThrow(() -> new RuntimeException("Dia não encontrado"));

    CalendarTimeEntity time = new CalendarTimeEntity();
    time.setTime(dto.getTime());
    time.setCalendarDay(day);

    return mapTimeToDTO(timeRepository.save(time));
  }

  @Transactional
  public void deleteTime(Long idDay, Long idTime) throws Exception {
    Long userId = AuthUtil.getAuthenticatedUserId();

    CalendarDayEntity day = dayRepository.findByIdAndUserId(idDay, userId)
          .orElseThrow(() -> new Exception("Dia não encontrado"));

    CalendarTimeEntity time = timeRepository.findById(idTime)
          .orElseThrow(() -> new Exception("Horário não encontrado"));

    if (!time.getCalendarDay().getId().equals(idDay)) {
      throw new Exception("Horário não pertence a esse dia");
    }

    timeRepository.delete(time);
  }


  private CalendarDayDTO mapToDTO(CalendarDayEntity day) {
    CalendarDayDTO dto = new CalendarDayDTO();
    dto.setId(day.getId());
    dto.setUserId(day.getUserId());
    dto.setLocalDate(day.getLocalDate());
    dto.setTimes(day.getTimes().stream()
          .sorted(Comparator.comparing(CalendarTimeEntity::getTime))
          .map(this::mapTimeToDTO)
          .toList());
    return dto;
  }

  private CalendarTimeDTO mapTimeToDTO(CalendarTimeEntity time) {
    CalendarTimeDTO dto = new CalendarTimeDTO();
    dto.setId(time.getId());
    dto.setTime(time.getTime());
    return dto;
  }
}