package com.codementor.comment.entity;

import com.codementor.comment.dto.CommentRequestDto;
import com.codementor.comment.dto.CommentResponseDto;
import com.codementor.member.entity.Member;
import com.codementor.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JoinColumn(nullable = false, name="post_id")
    private Long postId;

    @JoinColumn(nullable = false, name = "author_username")
    private String authorUsername;

    @Column(name="author_nickname")
    private String authorNickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private Comment parent;  // 부모 댓글 (null이면 일반 댓글)

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Comment> replies = new ArrayList<>();

    private int depth;  // 0이면 일반 댓글, 1이면 대댓글

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void delete() {
        this.isDeleted = "Y";
    }

    public void update(CommentRequestDto commentResponseDto) {
        this.content = commentResponseDto.getContent();
        this.updatedAt = LocalDateTime.now();
    }

    public void removeReply(Comment reply) {
        this.isDeleted = "Y";
        this.updatedAt = LocalDateTime.now();
        this.replies.remove(reply);  // 리스트에서 삭제
        reply.setParent(null);  // 부모와의 관계 끊기
    }
}
