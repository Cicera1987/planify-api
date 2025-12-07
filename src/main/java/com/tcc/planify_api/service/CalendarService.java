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

    List<CalendarDayEntity> savedDays = dayDTOs.stream().map(dto -> {

      CalendarDayEntity day = dayRepository.findByUserIdAndLocalDate(userId, dto.getLocalDate())
            .orElseGet(() -> {
              CalendarDayEntity newDay = new CalendarDayEntity();
              newDay.setUserId(userId);
              newDay.setLocalDate(dto.getLocalDate());
              newDay.setTimes(new ArrayList<>());
              return newDay;
            });

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
    }).toList();

    return savedDays.stream()
          .map(this::mapToDTO)
          .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<CalendarDayDTO> listDays(Long userId) {
    List<CalendarDayEntity> days = dayRepository.findByUserId(userId);

    return days.stream()
          .map(day -> {
            // Mapeia cada dia para o DTO
            CalendarDayDTO dto = new CalendarDayDTO();
            dto.setId(day.getId());
            dto.setUserId(day.getUserId());
            dto.setLocalDate(day.getLocalDate());

            List<CalendarTimeDTO> times = day.getTimes().stream()
                  .sorted(Comparator.comparing(CalendarTimeEntity::getTime))
                  .map(time -> {
                    boolean isOccupied = schedulingRepository.existsByCalendarTimeId(time.getId());

                    CalendarTimeDTO timeDTO = new CalendarTimeDTO();
                    timeDTO.setId(time.getId());
                    timeDTO.setTime(time.getTime());
                    timeDTO.setAvailable(!isOccupied);
                    return timeDTO;
                  })
                  .collect(Collectors.toList());

            dto.setTimes(times);
            return dto;
          })
          .toList();
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

    List<CalendarTimeDTO> sortedTimes = day.getTimes().stream()
          .sorted(Comparator.comparing(CalendarTimeEntity::getTime))
          .map(this::mapTimeToDTO)
          .collect(Collectors.toList());

    dto.setTimes(sortedTimes);
    return dto;
  }

  private CalendarTimeDTO mapTimeToDTO(CalendarTimeEntity time) {
    CalendarTimeDTO dto = new CalendarTimeDTO();
    dto.setId(time.getId());
    dto.setTime(time.getTime());
    return dto;
  }

}