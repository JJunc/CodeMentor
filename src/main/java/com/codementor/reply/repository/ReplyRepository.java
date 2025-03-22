package com.codementor.reply.repository;

import com.codementor.comment.entity.Comment;
import com.codementor.reply.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("SELECT r FROM Reply r WHERE r.member.username  = :username")
    Page<Reply> findByUsername(String username, Pageable pageable);
}
