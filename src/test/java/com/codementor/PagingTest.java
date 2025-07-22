package com.codementor;


import com.codementor.post.dto.PostListDto;
import com.codementor.post.entity.Post;
import com.codementor.post.enums.PostCategory;
import com.codementor.post.enums.PostSearchType;
import com.codementor.post.repository.PostRepository;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
class PagingTest {

    private static final Logger log = LoggerFactory.getLogger(PagingTest.class);
    @Autowired
    private PostRepository postRepository;


    @Test
    @DisplayName("마지막 페이지")
    void findByCategory_existingData() {
        // given
        PostCategory category = PostCategory.FREE;
        PageRequest pageRequest = PageRequest.of(99999, 10);

        // when
//        List<PostListDto> posts = postRepository.findByCategory(category, pageRequest);

        // then
        assertThat(posts).hasSize(10);
    }

    @Test
    @DisplayName("검색 테스트")
    void searchPost(){
        // given
        PostCategory category = PostCategory.FREE;
        PostSearchType searchType = PostSearchType.CONTENT;
        PageRequest pageRequest = PageRequest.of(999999, 10);
        String content ="스프링";

        // when
//        Page<PostListDto> page = postRepository.findByContentAndCategory(content,category,pageRequest);

        // then

    }


//    @Test
//    @DisplayName("No Offset 방식")
//    void NoOffset() {
//        PostCategory category = PostCategory.FREE;
//
//
//        Long cursorId = 3L;
//
//        int limit = 10;
//
//        // when
//        List<Post> posts = postRepository.findNextPage(
//                category, cursorId, limit
//        );
//
//        // then
//        assertThat(posts).isNotNull();
//
//        boolean hasNext = posts.size() == limit;
//        System.out.println("Has next page? " + hasNext);
//    }

}
