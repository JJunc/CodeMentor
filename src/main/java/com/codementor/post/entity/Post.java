package com.codementor.post.entity;

import com.codementor.comment.entity.Comment;
import com.codementor.member.entity.Member;
import com.codementor.post.dto.PostCreateDto;
import com.codementor.post.dto.PostDetailDto;
import com.codementor.post.dto.PostUpdateDto;
import com.codementor.post.enums.PostCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 50)
    private String authorUsername;

    @Column(nullable = false, length = 50)
    private String authorNickname;

    @Column(columnDefinition = "integer default 0")
    @ColumnDefault("0")
    private int views = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PostCategory category;

    @Column(nullable = false, length = 1)
    private Boolean isDeleted = false;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 수정 메서드 추가
    public void update(PostUpdateDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.category = dto.getCategory();
    }

    public void createPost(PostCreateDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.category = dto.getCategory();
        this.authorNickname = dto.getAuthorNickname();
    }

    public void increaseViews() {
        this.views += 1;  // Dirty Checking 활성화됨
    }
}
