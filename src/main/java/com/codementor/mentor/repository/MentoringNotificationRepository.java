package com.codementor.mentor.repository;

import com.codementor.mentor.entity.MentoringNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentoringNotificationRepository extends JpaRepository<MentoringNotification, Long> {
}
