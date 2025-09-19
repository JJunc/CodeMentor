package com.codementor.mentor.repository;

import com.codementor.mentor.entity.MentoringSchedule;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
    @Query(value = "select ms from MentoringSchedule ms where ms.id = :id")
    Optional<MentoringSchedule> findById(Long id);


}
