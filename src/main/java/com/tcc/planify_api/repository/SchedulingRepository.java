package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.CalendarTimeEntity;
import com.tcc.planify_api.entity.SchedulingEntity;
import com.tcc.planify_api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SchedulingRepository extends JpaRepository<SchedulingEntity, Long> {

  boolean existsByCalendarTimeAndProfessional(CalendarTimeEntity calendarTime, UserEntity professional);

  @Query("""
        SELECT s FROM SchedulingEntity s
        JOIN FETCH s.contact c
        JOIN FETCH s.calendarDay d
        JOIN FETCH s.calendarTime t
        WHERE s.professional.id = :professionalId
          AND s.status IN :activeStatuses
        ORDER BY d.localDate ASC, t.time ASC
    """)
  List<SchedulingEntity> findActiveSchedulings(Long professionalId, List<String> activeStatuses);

  @Query("""
        SELECT s FROM SchedulingEntity s
        JOIN FETCH s.contact c
        JOIN FETCH s.calendarDay d
        JOIN FETCH s.calendarTime t
        WHERE s.professional.id = :professionalId
          AND (:startDate IS NULL OR d.localDate >= :startDate)
          AND (:endDate IS NULL OR d.localDate <= :endDate)
          AND (:statuses IS NULL OR s.status IN :statuses)
        ORDER BY d.localDate ASC, t.time ASC
    """)
  List<SchedulingEntity> findSchedulingHistory(Long professionalId,
                                               LocalDate startDate,
                                               LocalDate endDate,
                                               List<String> statuses);

  @Query("SELECT s FROM SchedulingEntity s " +
        "WHERE s.professional.id = :professionalId " +
        "AND LOWER(s.contact.name) LIKE LOWER(CONCAT('%', :name, '%'))")
  List<SchedulingEntity> findByContactName(
        @Param("professionalId") Long professionalId,
        @Param("name") String name
  );
}