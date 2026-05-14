package com.munni.repository;

import com.munni.model.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
    List<CalendarEvent> findByCreatedByOrderByEventDateAsc(String createdBy);
    List<CalendarEvent> findAllByOrderByEventDateAsc();
}
