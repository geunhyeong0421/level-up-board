package com.gh.levelupboard.domain.comment;

import com.gh.levelupboard.config.QuerydslConfig;
import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.domain.user.User;
import com.gh.levelupboard.web.pagination.Pagination;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Import(QuerydslConfig.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    TestEntityManager em;
    @Autowired
    CommentRepository commentRepository;

    @DisplayName("댓글 페이징")
    @Test
    public void findByPostIdWithPagination() {
        //given
        User user = User.builder().build();
        em.persist(user);
        Post post = Post.builder().build();
        em.persist(post);
        for (int i = 1; i <= 100; i++) {
            Comment comment = Comment.builder()
                    .post(post)
                    .user(user)
                    .content("댓글 페이징 테스트 " + i)
                    .build();
            em.persist(comment);
        }

        //when
        int requestPage = 3; // 3번째 페이지 요청
        PageRequest pageRequest = PageRequest.of(requestPage - 1, Pagination.COMMENT.getSize());
        Page<Comment> result = commentRepository.findByPostIdWithPagination(post.getId(), pageRequest);

        //then
        List<Comment> comments = result.getContent();
        Pageable pageable = result.getPageable();
        int firstCommentNumber = pageable.getPageNumber() * pageable.getPageSize() + 1; // 2 * 20 + 1 = 41
        int lastCommentNumber = requestPage * pageable.getPageSize(); // 3 * 20 = 60

        assertThat(result.getTotalElements()).isEqualTo(100);
        assertThat(result.getTotalPages()).isEqualTo(5);
        assertThat(comments.get(0).getContent()).isEqualTo("댓글 페이징 테스트 " + firstCommentNumber);
        assertThat(comments.get(comments.size() - 1).getContent()).isEqualTo("댓글 페이징 테스트 " + lastCommentNumber);
    }

    @Test
    public void bulkSetPostNull() {
        //given
        Post post = Post.builder().build();
        em.persist(post);
        for (int i = 0; i < 10; i++) {
            Comment comment = Comment.builder()
                    .post(post)
                    .build();
            em.persist(comment);
        }
        List<Comment> comments = commentRepository.findAll();
        for (Comment comment : comments) {
            assertThat(comment.getPost()).isEqualTo(post);
        }

        //when
        int resultCount = commentRepository.bulkSetPostNull(post.getId());

        //then
        assertThat(resultCount).isEqualTo(comments.size());

        em.clear();
        comments = commentRepository.findAll();
        for (Comment comment : comments) {
            assertThat(comment.getPost()).isNull();
        }
    }


}