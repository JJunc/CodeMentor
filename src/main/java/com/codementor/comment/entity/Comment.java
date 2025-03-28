package com.codementor.comment.entity;

import com.codementor.comment.dto.CommentDto;
import com.codementor.member.entity.Member;
import com.codementor.post.entity.Post;
import com.codementor.reply.entity.Reply;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
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
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String isDeleted = "N";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name="post_id", referencedColumnName = "id")
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
