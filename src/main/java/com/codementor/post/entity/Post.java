package com.codementor.post.entity;

import com.codementor.comment.entity.Comment;
import com.codementor.member.entity.Member;
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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "author_username", referencedColumnName = "username")
    private Member author;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<PostImage> images = new ArrayList<>();

    @Column(columnDefinition = "integer default 0")
    @ColumnDefault("0")
    private int views = 0;

    @Enumerated(EnumType.STRING)
    private PostCategory category;

    private String isDeleted = "N";

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();;

    // 수정 메서드 추가
    public void update(PostUpdateDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.category = dto.getCategory();
    }

    public void increaseViews() {
        this.views += 1;  // Dirty Checking 활성화됨
    }
}
