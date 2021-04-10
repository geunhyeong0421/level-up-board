package com.gh.levelupboard.domain.comment;

import com.gh.levelupboard.config.QuerydslConfig;
import com.gh.levelupboard.domain.post.Post;
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


    @DisplayName("요청 정보(page, size)에 따른 댓글 페이징 결과를 확인할 수 있다.")
    @Test
    public void 댓글_페이징() {
        //given
        Post post = Post.builder().build();
        em.persist(post);

        int expectedTotal = 101;
        String baseContent = "댓글 페이징 테스트 ";
        for (int i = 1; i <= expectedTotal; i++) {
            Comment comment = Comment.builder()
                    .post(post)
                    .content(baseContent + i)
                    .build();
            em.persist(comment);
        }
        int requestPage = 3; // 요청 페이지
        int pageSize = Pagination.COMMENT.getSize(); // 20
        int expectedTotalPages = (int) Math.ceil(1.0 * expectedTotal / pageSize); // 6

        //when
        PageRequest pageRequest = PageRequest.of(requestPage - 1, pageSize);
        Page<Comment> result = commentRepository.findByPostIdWithPagination(post.getId(), pageRequest);

        //then
        List<Comment> comments = result.getContent();
        int firstCommentNumber = (requestPage - 1) * pageSize + 1; // 2 * 20 + 1 = 41
        int lastCommentNumber = requestPage * pageSize; // 3 * 20 = 60

        assertThat(result.getTotalElements()).isEqualTo(expectedTotal); // 101
        assertThat(result.getTotalPages()).isEqualTo(expectedTotalPages); // 6
        assertThat(comments.get(0).getContent()).isEqualTo(baseContent + firstCommentNumber); // "댓글 페이징 테스트 41"
        assertThat(comments.get(comments.size() - 1).getContent()).isEqualTo(baseContent + lastCommentNumber); // "댓글 페이징 테스트 60"
    }


    @DisplayName("벌크성 네이티브 쿼리로 외래키인 post_id를 null로 수정한다.")
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