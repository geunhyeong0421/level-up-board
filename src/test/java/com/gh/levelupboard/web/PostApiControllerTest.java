package com.gh.levelupboard.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.domain.post.PostRepository;
import com.gh.levelupboard.web.post.dto.PostSaveRequestDto;
import com.gh.levelupboard.web.post.dto.PostUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostRepository postRepository;

    @AfterEach
    public void tearDown() throws Exception {
        postRepository.deleteAll();
    }

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @Test
    @WithMockUser(roles = "USER")
    public void Post_등록된다() throws Exception {
        //given
        String title = "title";
        String content = "content";
        PostSaveRequestDto requestDto = new PostSaveRequestDto(title, content, "writer");

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
//        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Post> all = postRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }


    @Test
    @WithMockUser(roles = "USER")
    public void Post_수정된다() throws Exception {
        //given
        Post savedPost = postRepository.save(new Post("title", "content", "writer"));

        Long updateId = savedPost.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostUpdateRequestDto requestDto = new PostUpdateRequestDto(expectedTitle, expectedContent);

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

//        HttpEntity<PostUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
//        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Post> all = postRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }



}