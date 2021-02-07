package com.gh.levelupboard.domain.post;

import com.gh.levelupboard.domain.user.LoginType;
import com.gh.levelupboard.domain.user.Role;
import com.gh.levelupboard.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    EntityManager em;

    User user;

    @PostConstruct
    public void setup() {
        user = User.builder()
                .loginType(LoginType.GOOGLE)
                .loginId("1234")
                .name("test")
                .email("test@gmail.com")
                .picture(null)
                .role(Role.USER)
                .build();
    }


    @Test
    public void saveTest() throws Exception {
    /**
     * GenerationType.IDENTITY 설정으로 인해 persist()와 동시에 쿼리가 날라간다..
     */
        //given
        em.persist(user); // 영속성 컨텍스트에 포함되면서 pk인 id값 생성과 insert 쿼리 발생
        String title = "title";
        String content = "content";

        //when
        Post savedPost = postRepository.save(new Post(title, content, user));

        //then
        assertThat(savedPost.getTitle()).isEqualTo(title);
        assertThat(savedPost.getContent()).isEqualTo(content);
    }


    @Test
    public void findAllDescTest() throws Exception {
    /**
     * GenerationType.IDENTITY 설정으로 인해 persist()와 동시에 쿼리가 날라간다..
     */
        //given
        em.persist(user);
        postRepository.save(new Post("title1", "content1", user));
        postRepository.save(new Post("title2", "content2", user));
        postRepository.save(new Post("title3", "content3", user));

        //when
        List<Post> postList = postRepository.findAllDesc(); // 등록 순서의 역순으로 조회

        //then
        assertThat(postList)
                .extracting("title")
                .containsExactly("title3", "title2", "title1");
    }


}