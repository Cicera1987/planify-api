package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.CalendarDayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarDayRepository extends JpaRepository<CalendarDayEntity, Long> {}
