package com.gh.levelupboard.domain.post;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

//    @Test
//    public void 게시글저장_불러오기() throws Exception {
//        //given
//        String title = "테스트 게시글";
//        String content = "테스트 본문";
//
//        postRepository.save(new Post(title, content, "geunhyeong0421@gmail.com"));
//
//        //when
//        List<Post> postList = postRepository.findAll();
//
//        //then
//        Post post = postList.get(0);
//        Assertions.assertThat(post.getTitle()).isEqualTo(title);
//        Assertions.assertThat(post.getContent()).isEqualTo(content);
//    }

}