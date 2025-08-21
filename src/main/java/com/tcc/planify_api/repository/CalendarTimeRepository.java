package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.CalendarTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarTimeRepository extends JpaRepository<CalendarTimeEntity, Long> {}

