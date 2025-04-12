package com.codementor;


import com.codementor.post.entity.Post;
import com.codementor.post.enums.PostCategory;
import com.codementor.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PagingTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("기존 페이징 방식")
    void findByCategory_existingData() {
        // given
        PostCategory category = PostCategory.FREE;
        PageRequest pageRequest = PageRequest.of(2000, 10, Sort.by(Sort.Direction.DESC, "createdAt"));  // 예: 2000번째 페이지

        // when
        Page<Post> page = postRepository.findByCategoryAndNotDeleted(category, pageRequest);

        // then
        assertThat(page).isNotNull();
        assertThat(page.getContent().size()).isLessThanOrEqualTo(10); // 마지막 페이지일 수 있으므로
        assertThat(page.getNumber()).isEqualTo(2000);
    }
}
