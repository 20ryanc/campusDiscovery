package com.gtbackend.gtbackend.dao;

import com.gtbackend.gtbackend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query(value = "SELECT e FROM Event e WHERE e.id >= ?1 AND e.id <= ?2 ORDER BY e.id")
    List<Event> findEventByIdRange(Integer start, Integer end);

    @Query(value = "SELECT e FROM Event e WHERE e.email = ?1 ORDER BY e.id")
    List<Event> findEventByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Event e WHERE e.email = ?1")
    void deleteAllByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Event e WHERE e.id = ?1")
    void deleteEvent(Integer id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Event e SET e.title = ?2 WHERE e.id = ?1")
    void updateTitle(Integer id, String title);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Event e SET e.email = ?2 WHERE e.id = ?1")
    void updateEmail(Integer id, String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Event e SET e.description = ?2 WHERE e.id = ?1")
    void updateDescription(Integer id, String description);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Event e SET e.location = ?2 WHERE e.id = ?1")
    void updateLocation(Integer id, String location);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Event e SET e.time = ?2 WHERE e.id = ?1")
    void updateTime(Integer id, String time);
}