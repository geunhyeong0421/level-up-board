package com.gh.levelupboard.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.domain.post.PostRepository;
import com.gh.levelupboard.domain.user.LoginType;
import com.gh.levelupboard.domain.user.Role;
import com.gh.levelupboard.domain.user.User;
import com.gh.levelupboard.domain.user.UserRepository;
import com.gh.levelupboard.web.post.dto.PostSaveRequestDto;
import com.gh.levelupboard.web.post.dto.PostUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;
    private User savedUser;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        User user = User.builder()
                .loginType(LoginType.GOOGLE)
                .loginId("1234")
                .name("test")
                .email("test@gmail.com")
                .picture(null)
                .role(Role.USER)
                .build();
        savedUser = userRepository.save(user);
    }

    @AfterEach
    public void tearDown() throws Exception {
        postRepository.deleteAll();
    }


    @Test
    @WithMockUser(roles = "USER")
    public void Post_등록된다() throws Exception {
        //given
        String title = "title";
        String content = "content";
        PostSaveRequestDto requestDto = new PostSaveRequestDto(title, content, savedUser.getId());

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Post> all = postRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Post_수정된다() throws Exception {
        //given
        Post savedPost = postRepository.save(new Post("title", "content", savedUser));
        Long postId = savedPost.getId();

        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostUpdateRequestDto requestDto = new PostUpdateRequestDto(expectedTitle, expectedContent);

        String url = "http://localhost:" + port + "/api/v1/posts/" + postId;

        //when
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        Post post = postRepository.findById(postId).get();
        assertThat(post.getTitle()).isEqualTo(expectedTitle);
        assertThat(post.getContent()).isEqualTo(expectedContent);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Post_삭제된다() throws Exception {
        //given
        Post savedPost = postRepository.save(new Post("title", "content", savedUser));
        Long postId = savedPost.getId();

        String url = "http://localhost:" + port + "/api/v1/posts/" + postId;

        //when
        mvc.perform(delete(url))
                .andExpect(status().isOk());

        //then
        Optional<Post> result = postRepository.findById(postId);
        assertThat(result.isEmpty()).isTrue();
    }



}