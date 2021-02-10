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

    private static final String TEST_USER_NAME = "김테스트";
    private static final String TEST_BOARD_NAME = "테스트 게시판";
    private User testUser;
    private Board testBoard;

    @PostConstruct
    public void setUp() {
        testUser = User.builder()
                .loginType(LoginType.GOOGLE)
                .loginId("1234")
                .name(TEST_USER_NAME)
                .email("test@gmail.com")
                .picture(null)
                .role(Role.USER)
                .build();
        testBoard = new Board(TEST_BOARD_NAME);
    }


    @Test
    public void saveTest() throws Exception {
        //given
        em.persist(testUser);
        em.persist(testBoard);
        String title = "title";
        String content = "content";

        //when
        Post savedPost = postRepository.save(new Post(title, content, testUser, testBoard));

        //then
        assertThat(savedPost.getTitle()).isEqualTo(title);
        assertThat(savedPost.getContent()).isEqualTo(content);
        assertThat(savedPost.getUser().getName()).isEqualTo(TEST_USER_NAME);
        assertThat(savedPost.getBoard().getName()).isEqualTo(TEST_BOARD_NAME);
    }


    @Test
    public void findAllDescTest() throws Exception {
        //given
        em.persist(testUser);
        em.persist(testBoard);
        postRepository.save(new Post("자유1", "content1", testUser));
        postRepository.save(new Post("테스트1", "content2", testUser, testBoard));
        postRepository.save(new Post("자유2", "content3", testUser));
        postRepository.save(new Post("테스트2", "content4", testUser, testBoard));

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