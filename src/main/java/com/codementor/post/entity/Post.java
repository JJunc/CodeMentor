package com.codementor.post.entity;

import com.codementor.comment.entity.Comment;
import com.codementor.member.entity.Member;
import com.codementor.post.dto.PostCreateDto;
import com.codementor.post.dto.PostDetailDto;
import com.codementor.post.dto.PostListDto;
import com.codementor.post.dto.PostUpdateDto;
import com.codementor.post.enums.PostCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
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
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@NamedNativeQuery(
        name = "Post.findByTitleAndCategory",
        resultSetMapping = "PostListDtoMapping",
        query = """
                    SELECT
                       id, 
                       title,
                       author_nickname AS authorNickname,  
                       views AS views, 
                       category,
                       created_at AS createdAt,            
                       updated_at AS updatedAt,            
                       is_deleted AS isDeleted
                   FROM post
                   WHERE category = :category 
                     AND is_deleted = false
                     AND MATCH (title) AGAINST (CONCAT(:keyword, '*') IN BOOLEAN MODE)
                   ORDER BY id DESC
                   LIMIT :limit OFFSET :offset
                """
)
@NamedNativeQuery(
        name = "Post.postCountByTitleAndCategory",
        query = """
                SELECT COUNT(p.id) 
                FROM post p
                WHERE p.category = :category AND p.is_deleted = false
                AND MATCH (p.title) AGAINST (CONCAT(:keyword, '*') IN BOOLEAN MODE)
                """
)
@NamedNativeQuery(
        name = "Post.findPostListDtoByCategory",
        resultSetMapping = "PostListDtoMapping",
        query = """
                SELECT p.id, p.title, p.author_nickname AS authorNickname, p.views, p.category, 
                       p.created_at AS createdAt, p.updated_at AS updatedAt, p.is_deleted AS isDeleted
                FROM post p
                JOIN (SELECT id
                    FROM post
                    WHERE category = :category
                    AND is_deleted = false
                    ORDER BY id DESC
                    LIMIT :limit OFFSET :offset) temp ON temp.id = p.id
                """
)

@SqlResultSetMapping(
        name = "PostListDtoMapping",
        classes = @ConstructorResult(
                targetClass = PostListDto.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "title", type = String.class),
                        @ColumnResult(name = "authorNickname", type = String.class),
                        @ColumnResult(name = "views", type = Integer.class),
                        @ColumnResult(name = "category", type = PostCategory.class),
                        @ColumnResult(name = "createdAt", type = LocalDateTime.class),
                        @ColumnResult(name = "updatedAt", type = LocalDateTime.class),
                        @ColumnResult(name = "isDeleted", type = Boolean.class)
                }
        )
)
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
