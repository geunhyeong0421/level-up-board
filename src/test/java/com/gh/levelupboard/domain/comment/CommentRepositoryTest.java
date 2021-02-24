package com.gh.levelupboard.domain.comment;

import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    TestEntityManager em;


    @Test
    public void findByPostId() {
        //given
        User testUser = User.builder().build();
        em.persist(testUser);
        Post testPost = new Post(null, null, testUser, "댓글 잘 달리나", "테스트 중");
        em.persist(testPost);
        int count = 0;
        for (int i = 1; i <= 10; i++) {
            Comment parent = Comment.builder()
                    .post(testPost)
                    .user(testUser)
                    .content(i + "번째 댓글")
                    .build();
            em.persist(parent); // 댓글 등록
            em.flush(); // @PostPersist 변경 내용 적용 - update 쿼리 발생
            count++;
            if (i % 2 == 0) {
                continue; // 짝수번째 댓글에는 대댓글을 달지 않음
            }
            for (int j = 0; j < i; j++) { // 댓글의 순번만큼 반복
                Comment child = Comment.builder()
                        .post(testPost)
                        .user(testUser)
                        .content(parent.getId() + "번 댓글의 대댓글: " + j)
                        .build();
                child.setParent(parent);
                em.persist(child); // 대댓글 등록
                count++;
            }
        }
        System.out.println("\n\n======================== 입력 끝 =========================\n\n");

        //when
        List<Comment> comments = commentRepository.findByPostId(testPost.getId());

        //then
        assertThat(comments.size()).isEqualTo(count);
        for (Comment comment : comments) {
            System.out.printf("groupId:%d\tid: %d\tcontent: %s\n", comment.getGroupId(), comment.getId(), comment.getContent());
        }

    }


}