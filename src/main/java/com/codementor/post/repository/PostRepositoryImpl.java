package com.codementor.post.repository;

import com.codementor.post.dto.PostListDto;
import com.codementor.post.enums.PostCategory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<PostListDto> findByCategory(PostCategory category, int limit, int offset) {
        return em.createNamedQuery("Post.findPostListDtoByCategory", PostListDto.class)
                .setParameter("category", category.name())
                .setParameter("limit", limit)
                .setParameter("offset", offset)
                .getResultList();
    }

    @Override
    public List<PostListDto> findByTitleAndCategory(String keyword, PostCategory category, int limit, int offset) {
        return em.createNamedQuery("Post.findByTitleAndCategory", PostListDto.class)
                .setParameter("keyword", keyword)
                .setParameter("category", category.name())
                .setParameter("limit", limit)
                .setParameter("offset", offset)
                .getResultList();
    }

    @Override
    public Long postCountByTitleAndCategory(String keyword, PostCategory category) {
        return ((Number) em.createNamedQuery("Post.postCountByTitleAndCategory")
                .setParameter("category", category.name())
                .setParameter("keyword", keyword)
                .getSingleResult()).longValue();
    }

//    @Override
//    public List<PostListDto> findByCategory(PostCategory category, int limit, int offset) {
//        String sql = """
//            SELECT p.id, p.title, p.author_nickname as authorNickname, p.views, p.category,
//                  p.created_at AS createdAt,
//                   p.updated_at AS updatedAt,
//                   p.is_deleted as isDeleted
//            FROM post p
//            JOIN (SELECT p1.id
//                FROM post p1
//                WHERE p1.category = :category AND p1.is_deleted = false
//                ORDER BY p1.id DESC
//                LIMIT :limit OFFSET :offset) temp ON p.id = temp.id
//        """;
//
//        return em.createNativeQuery(sql, "PostListDtoMapping")
//                .setParameter("category", category)
//                .setParameter("limit", limit)
//                .setParameter("offset", offset)
//                .getResultList();
//    }
}
