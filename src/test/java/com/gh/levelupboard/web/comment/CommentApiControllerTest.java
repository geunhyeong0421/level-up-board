package com.gh.levelupboard.web.comment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gh.levelupboard.config.auth.dto.SessionUser;
import com.gh.levelupboard.domain.comment.Comment;
import com.gh.levelupboard.domain.comment.CommentRepository;
import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.domain.post.PostRepository;
import com.gh.levelupboard.domain.user.User;
import com.gh.levelupboard.domain.user.UserRepository;
import com.gh.levelupboard.web.comment.dto.CommentListResponseDto;
import com.gh.levelupboard.web.comment.dto.CommentSaveRequestDto;
import com.gh.levelupboard.web.comment.dto.CommentUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private Post testPost;
    private User testUser;
    private Comment testComment;

    private MockMvc mvc;

    @MockBean(name = "httpSession")
    private HttpSession httpSession;


    @PostConstruct
    public void setUp() {
        User user = User.builder()
                .name("김테스트")
                .build();
        testUser = userRepository.save(user);
        when(httpSession.getAttribute("user")).thenReturn(new SessionUser(testUser));
        Post post = Post.builder()
                .user(testUser)
                .title("테스트 제목")
                .content("테스트 내용")
                .build();
        testPost = postRepository.save(post);
    }
    @BeforeEach
    public void setUpEach() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .addFilters(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysExpect(status().isOk())
                .alwaysDo(print())
                .build();

        Comment comment = Comment.builder()
                .post(testPost)
                .user(testUser)
                .content("부모가 없는 기본 댓글입니다.")
                .build();
        testComment = commentRepository.save(comment);
    }

    @AfterEach
    public void tearDown() {
        commentRepository.deleteAll();
    }


    @WithMockUser(roles = "USER")
    @Test
    public void addComment() throws Exception {
        //given
        String expectedContent = "비밀 대댓글을 등록합니다.";
        CommentSaveRequestDto requestDto = CommentSaveRequestDto.builder()
                .postId(testPost.getId())
                .userId(testUser.getId())
                .parentId(testComment.getId())
                .content(expectedContent)
                .isSecret(true)
                .build();
        String url = "http://localhost:" + port + "/api/v1/comments";

        //when
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)));

        //then
        List<Comment> comments = commentRepository.findAll();
        Comment newComment = comments.get(1);
        assertThat(newComment.getContent()).isEqualTo(expectedContent);
        assertThat(newComment.getParent().getContent()).isEqualTo(testComment.getContent());
        assertThat(newComment.isSecret()).isTrue();
    }

    @WithMockUser(roles = "USER")
    @Test
    public void modifyComment() throws Exception {
        //given
        Long commentId = testComment.getId();
        String expectedContent = "테스트 댓글을 비밀 댓글로 수정합니다.";
        CommentUpdateRequestDto requestDto = CommentUpdateRequestDto.builder()
                .id(commentId)
                .content(expectedContent)
                .isSecret(true)
                .build();
        String url = "http://localhost:" + port + "/api/v1/comments/" + commentId;

        //when
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)));

        //then
        Comment updatedComment = commentRepository.findById(commentId).get();
        assertThat(updatedComment.getContent()).isEqualTo(expectedContent);
        assertThat(updatedComment.isSecret()).isTrue();
    }

    @WithMockUser(roles = "USER")
    @Test
    public void removeComment() throws Exception {
        //given
        Long commentId = testComment.getId();
        String url = "http://localhost:" + port + "/api/v1/comments/" + commentId;

        //when
        mvc.perform(delete(url));

        //then
        Optional<Comment> comments = commentRepository.findById(commentId);
        assertThat(comments.isEmpty()).isTrue();
    }

    @WithMockUser(roles = "USER")
    @Test
    public void getCommentList() throws Exception {
        //given
        long count = commentRepository.count();
        for (int i = 1; i <= 10; i++) {
            Comment parent = Comment.builder()
                    .post(testPost)
                    .user(testUser)
                    .content(i + "번째 추가 댓글")
                    .build();
            commentRepository.save(parent); // 댓글 등록
            count++;
            if (i % 2 == 0) {
                continue; // 짝수번째 댓글에는 대댓글을 달지 않음
            }
            for (int j = 0; j < i; j++) { // 추가 댓글의 순번만큼 반복
                Comment child = Comment.builder()
                        .post(testPost)
                        .user(testUser)
                        .content(parent.getId() + "번 댓글의 대댓글: " + j)
                        .build();
                child.setParent(parent);
                commentRepository.save(child); // 대댓글 등록
                count++;
            }
        }

        Long postId = testPost.getId();
        String url = "http://localhost:" + port + "/api/v1/posts/" + postId + "/comments";

        //when
        MvcResult result = mvc.perform(get(url)).andReturn();

        //then
        List<CommentListResponseDto> resultList = new ObjectMapper().readValue(result.getResponse().getContentAsString()
                , new TypeReference<List<CommentListResponseDto>>() {});

        assertThat(resultList.size()).isEqualTo(count);
        for (CommentListResponseDto dto : resultList) {
            System.out.printf("parentId: %d\tid: %d\tisModified: %b\tisMyComment: %b\tcontent: %s\n", dto.getParentId(), dto.getId(), dto.getIsModified(), dto.getIsMyComment(), dto.getContent());
        }
    }


}