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

    @Query("SELECT c FROM Comment c WHERE c.authorUsername = :username")
    Page<Comment> findByUsername(@Param("username") String username, Pageable pageable);

    @Query("SELECT c FROM Comment c  WHERE c.postId = :postId")
    Page<Comment> findByPostId(Long postId, Pageable pageable);

    @Query("SELECT c FROM Comment c, Post p WHERE p.title = :keyword")
    Page<Comment> findByPostTitle(String keyword, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.authorUsername = :keyword")
    Page<Comment> findByAuthorUsername(String keyword, Pageable pageable);

    Page<Comment> findByContent(String keyword, Pageable pageable);

    Page<Comment> findAll(Pageable pageable);


}
