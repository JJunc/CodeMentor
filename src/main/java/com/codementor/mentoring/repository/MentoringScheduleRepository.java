package com.codementor.mentoring.repository;

import com.codementor.mentoring.entity.MentoringSchedule;
import com.codementor.mentoring.enums.MentoringScheduleStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MentoringScheduleRepository extends JpaRepository<MentoringSchedule, Long> {

    @Query(value = """
    SELECT *
    FROM mentoring_schedule ms
    WHERE ms.id = :id
    for update
""", nativeQuery = true)
    Optional<MentoringSchedule> findByIdPL(Long id);

    @Lock(LockModeType.OPTIMISTIC)
    Optional<MentoringSchedule> findById(Long id);

    @Modifying
    @Query("""
    UPDATE MentoringSchedule
    SET status = :status
    WHERE id = :scheduleId
""")
    int bookSchedule(Long scheduleId, MentoringScheduleStatus status);

    List<MentoringSchedule> findByMentorIdAndDate(Long mentorId, LocalDateTime date);


}
