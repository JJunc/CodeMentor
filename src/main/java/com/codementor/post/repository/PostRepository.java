package com.codementor.post.repository;

import com.codementor.post.dto.PostListDto;
import com.codementor.post.entity.Post;
import com.codementor.post.enums.PostCategory;
import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


    Optional<Post> findById(Long id);

    @Query(value = """
            SELECT new com.codementor.post.dto.PostListDto(p.id, p.title, p.authorNickname,
                p.views, p.category, p.createdAt, p.updatedAt, p.isDeleted)
            FROM Post p
            WHERE p.category = :category
            AND p.isDeleted = false 
            ORDER BY p.id DESC
            LIMIT :limit OFFSET :offset
            """)
    List<PostListDto> findByCategory(@Param("category") PostCategory category,
                                     @Param("limit") int limit,
                                     @Param("offset") int offset);


    @Query("SELECT COUNT(p) FROM Post p WHERE p.category = :category AND p.isDeleted = false ")
    Long countByCategory(@Param("category") PostCategory category);

    // 검색
    Page<Post> findByCategoryOrderById(PostCategory category, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.category = :category AND p.authorUsername = :authorUsername")
    Page<Post> findByCategoryAndAuthor(PostCategory category, String authorUsername, Pageable pageable);

    Page<Post> findByContentAndCategory(String content, PostCategory category, Pageable pageable);

    @Query(value = """
            SELECT
               *
                FROM post p
                WHERE p.category = :category 
                          AND p.is_deleted = false
                          AND MATCH (p.title) AGAINST (CONCAT(:title, '*') IN BOOLEAN MODE)
                ORDER BY p.created_at DESC
            """
            , nativeQuery = true)
    Page<Post> findByTitleAndCategory(
            @Param("title") String title,
            @Param("category") PostCategory category,
            Pageable pageable);

    @Query(value = """
            SELECT new com.codementor.post.dto.PostListDto(p.id, p.title, p.authorNickname,
                p.views, p.category, p.createdAt, p.updatedAt, p.isDeleted)
            FROM Post p
            WHERE p.category = :category
                        AND p.title = :keyword
            AND p.isDeleted = false 
            ORDER BY p.id DESC
            LIMIT :limit OFFSET :offset
            """)
    List<PostListDto> findByTitleAndCategory(
                                     @Param("keyword") String keyword,
                                     @Param("category") PostCategory category,
                                     @Param("limit") int limit,
                                     @Param("offset") int offset);

    Page<PostListDto> searchByTitleAndCategory(String title, PostCategory category, Pageable pageable);


    Page<Post> findByTitleOrContentAndCategory(String title, String content, PostCategory category, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.category = :category AND p.authorUsername = :authorUsername")
    Page<Post> findByAuthorAndCategory(String authorUsername, PostCategory category, Pageable pageable);

    @Modifying
    @Query("UPDATE Post p SET p.authorNickname = :newNickname WHERE p.authorUsername = :username")
    void updateAuthorNickname(@Param("username") String username, @Param("newNickname") String newNickname);

    @Modifying
    @Query("UPDATE Post p SET p.views = p.views + 1 WHERE p.id = :postId")
    void incrementViewCount(@Param("postId") Long postId);
}
