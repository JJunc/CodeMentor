package com.codementor.post.repository;

import com.codementor.post.entity.Post;
import com.codementor.post.enums.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long id);

    Page<Post> findByCategory(PostCategory category, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.category = :category AND p.author.username = :author")
    Page<Post> findByCategoryAndAuthor(PostCategory category, String author, Pageable pageable);
}
