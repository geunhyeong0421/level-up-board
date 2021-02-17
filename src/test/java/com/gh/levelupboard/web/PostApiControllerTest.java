//package com.gh.levelupboard.web;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.gh.levelupboard.domain.board.Board;
//import com.gh.levelupboard.domain.board.BoardRepository;
//import com.gh.levelupboard.domain.post.Post;
//import com.gh.levelupboard.domain.post.PostRepository;
//import com.gh.levelupboard.domain.user.LoginType;
//import com.gh.levelupboard.domain.user.Role;
//import com.gh.levelupboard.domain.user.User;
//import com.gh.levelupboard.domain.user.UserRepository;
//import com.gh.levelupboard.web.post.dto.PostSaveRequestDto;
//import com.gh.levelupboard.web.post.dto.PostUpdateRequestDto;
//import com.nimbusds.jose.util.StandardCharset;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.http.*;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.filter.CharacterEncodingFilter;
//
//import javax.annotation.PostConstruct;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class PostApiControllerTest {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private PostRepository postRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private BoardRepository boardRepository;
//
//    @Autowired
//    private WebApplicationContext context;
//
//    private User testUser;
//    private Board testBoard;
//
//    private MockMvc mvc;
//
//
//    @PostConstruct
//    public void setUp() {
//        User user = User.builder()
//                .loginType(LoginType.GOOGLE)
//                .loginId("1234")
//                .name("김테스트")
//                .email("test@gmail.com")
//                .picture(null)
//                .role(Role.USER)
//                .build();
//        testUser = userRepository.save(user);
//        testBoard = boardRepository.save(new Board("테스트 게시판"));
//    }
//    @BeforeEach
//    public void setUpEach() {
//        mvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .addFilters(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
//                .alwaysExpect(status().isOk())
//                .alwaysDo(print())
//                .build();
//    }
//    @AfterEach
//    public void tearDown() {
//        postRepository.deleteAll();
//    }
//
//
//
//    @Test
//    @WithMockUser(roles = "USER")
//    public void addPost() throws Exception {
//        //given
//        String title = "title";
//        String content = "content";
//        PostSaveRequestDto requestDto = new PostSaveRequestDto(testUser.getId(), title, content);
//
//        String url = "http://localhost:" + port + "/api/v1/posts";
//
//        //when
//        mvc.perform(post(url)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(requestDto)));
//
//        //then
//        List<Post> all = postRepository.findAll();
//        assertThat(all.get(0).getTitle()).isEqualTo(title);
//        assertThat(all.get(0).getContent()).isEqualTo(content);
//    }
//
//    @Test
//    @WithMockUser(roles = "USER")
//    public void modifyPost() throws Exception {
//        //given
//        Post savedPost = postRepository.save(new Post(null, testUser, "title", "content"));
//        Long postId = savedPost.getId();
//
//        String expectedTitle = "title2";
//        String expectedContent = "content2";
//
//        PostUpdateRequestDto requestDto = new PostUpdateRequestDto(expectedTitle, expectedContent);
//
//        String url = "http://localhost:" + port + "/api/v1/posts/" + postId;
//
//        //when
//        mvc.perform(put(url)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(requestDto)));
//
//        //then
//        Post post = postRepository.findById(postId).get();
//        assertThat(post.getTitle()).isEqualTo(expectedTitle);
//        assertThat(post.getContent()).isEqualTo(expectedContent);
//    }
//
//    @Test
//    @WithMockUser(roles = "USER")
//    public void removePost() throws Exception {
//        //given
//        Post savedPost = postRepository.save(new Post(null, testUser, "title", "content"));
//        Long postId = savedPost.getId();
//
//        String url = "http://localhost:" + port + "/api/v1/posts/" + postId;
//
//        //when
//        mvc.perform(delete(url));
//
//        //then
//        Optional<Post> result = postRepository.findById(postId);
//        assertThat(result.isEmpty()).isTrue();
//    }
//
//
//
//}
