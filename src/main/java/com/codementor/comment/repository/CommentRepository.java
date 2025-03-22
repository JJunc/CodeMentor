package com.codementor.comment.repository;

import com.codementor.comment.entity.Comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c JOIN FETCH c.member WHERE c.member.username = :username")
    Page<Comment> findByUsername(@Param("username") String username, Pageable pageable);

    Page<Comment> findByPostId(Long id, Pageable pageable);

    List<Comment> findByPostId(Long id);

}
