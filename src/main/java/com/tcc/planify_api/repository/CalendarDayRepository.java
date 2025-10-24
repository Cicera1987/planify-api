package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.CalendarDayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalendarDayRepository extends JpaRepository<CalendarDayEntity, Long> {
  Optional<CalendarDayEntity> findByUserIdAndLocalDate(Long userId, LocalDate localDate);
  List<CalendarDayEntity> findByUserId(Long useId);
}
