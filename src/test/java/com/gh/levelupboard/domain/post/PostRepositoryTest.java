package com.gh.levelupboard.domain.post;

import com.gh.levelupboard.domain.board.Board;
import com.gh.levelupboard.domain.user.LoginType;
import com.gh.levelupboard.domain.user.Role;
import com.gh.levelupboard.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TestEntityManager em;

    private User testUser;
    private Board testBoard;

    @PostConstruct
    public void setUp() {
        testUser = User.builder()
                .loginType(LoginType.GOOGLE)
                .loginId("1234")
                .name("김테스트")
                .email("test@gmail.com")
                .role(Role.USER)
                .build();
        testBoard = new Board("테스트 게시판");
    }


    @Test
    public void findAllDesc() {
        //given
        em.persist(testUser);
        em.persist(testBoard);
        postRepository.save(new Post(null, null, testUser, "자유1", "content1"));
        postRepository.save(new Post(null, testBoard, testUser, "테스트1", "content2"));
        postRepository.save(new Post(null, null, testUser, "자유2", "content3"));
        postRepository.save(new Post(null, testBoard, testUser, "테스트2", "content4"));

        //when
        List<Post> postList = postRepository.findAllDesc(); // 등록 순서의 역순으로 조회

        //then
        assertThat(postList)
                .extracting("title")
                .containsExactly("테스트2", "자유2", "테스트1", "자유1");
        assertThat(postList)
                .extracting("content")
                .containsExactly("content4", "content3", "content2", "content1");
    }


}
