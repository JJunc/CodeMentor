package com.codementor.reply.entity;

import com.codementor.comment.dto.CommentDto;
import com.codementor.comment.entity.Comment;
import com.codementor.member.entity.Member;
import com.codementor.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String isDeleted = "N";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = false)
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "author_username", referencedColumnName = "username")
    private Member member;

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

    public void delete() {
        this.isDeleted = "Y";
        this.content = "삭제된 댓글입니다.";
    }

    public void update(CommentDto commentDto) {
        this.content = commentDto.getContent();
        this.updatedAt = LocalDateTime.now();
    }
}
