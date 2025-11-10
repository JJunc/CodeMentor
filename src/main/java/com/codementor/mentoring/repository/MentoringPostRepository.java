package com.codementor.mentoring.repository;

import com.codementor.mentoring.dto.MentoringPostListDto;
import com.codementor.mentoring.entity.MentoringPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentoringPostRepository extends JpaRepository<MentoringPost, Long> {
}
