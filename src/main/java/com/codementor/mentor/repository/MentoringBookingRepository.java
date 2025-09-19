package com.codementor.mentor.repository;

import com.codementor.mentor.entity.MentoringBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentoringBookingRepository extends JpaRepository<MentoringBooking, Long> {

    Optional<MentoringBooking> findByMentoringScheduleId(Long scheduleId);
    List<MentoringBooking> findAllByMentoringScheduleId(Long scheduleId);
}
