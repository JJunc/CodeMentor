package com.codementor.post.repository;

import com.codementor.member.dto.MemberSearchDto;
import com.codementor.member.entity.Member;
import com.codementor.post.dto.PostSearchDto;
import com.codementor.post.entity.Post;
import com.codementor.post.enums.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long id);

    @Query("SELECT p FROM Post p WHERE p.category = :category AND p.isDeleted = 'N'")
    Page<Post> findByCategoryAndNotDeleted(@Param("category") PostCategory category, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.category = :category AND p.isDeleted = 'N'")
    List<Post> findByCategoryAndNotDeleted(@Param("category") PostCategory category);

    Page<Post> findByCategory(PostCategory category, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.category = :category AND p.author.username = :author")
    Page<Post> findByCategoryAndAuthor(PostCategory category, String author, Pageable pageable);

    Page<Post> findByContentAndCategory(String content, PostCategory category, Pageable pageable);
    Page<Post> findByTitleAndCategory(String title, PostCategory category,Pageable pageable);
    Page<Post> findByTitleOrContentAndCategory(String title, String content, PostCategory category, Pageable pageable);
    Page<Post> findByAuthorAndCategory(String author, PostCategory category, Pageable pageable);

}
