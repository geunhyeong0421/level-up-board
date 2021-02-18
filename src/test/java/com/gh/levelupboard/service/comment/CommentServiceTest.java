package com.gh.levelupboard.service.comment;

import com.gh.levelupboard.domain.comment.Comment;
import com.gh.levelupboard.domain.comment.CommentRepository;
import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.domain.post.PostRepository;
import com.gh.levelupboard.domain.user.User;
import com.gh.levelupboard.domain.user.UserRepository;
import com.gh.levelupboard.web.comment.dto.CommentListResponseDto;
import com.gh.levelupboard.web.comment.dto.CommentSaveRequestDto;
import com.gh.levelupboard.web.comment.dto.CommentUpdateRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;


    @Test
    public void add() {
        //given
        Long expectedCommentId = 10L;
        CommentSaveRequestDto dto = CommentSaveRequestDto.builder()
                .postId(1L)
                .userId(2L)
                .parentId(3L)
                .content("10번째 댓글입니다.")
                .build();
        Comment savedComment = Comment.builder()
                .id(expectedCommentId)
                .build();
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(Post.builder().build()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder().build()));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(Comment.builder().build()));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        //when
        Long result = commentService.add(dto);

        //then
        assertThat(result).isEqualTo(expectedCommentId);
    }

    @Test
    public void modify() {
        //given
        Long expectedCommentId = 10L;
        String expectedContent = "10번 댓글을 비밀 댓글로 수정!";
        CommentUpdateRequestDto dto = CommentUpdateRequestDto.builder()
                .id(expectedCommentId)
                .content(expectedContent)
                .isSecret(true)
                .build();
        Comment targetComment = Comment.builder()
                .id(expectedCommentId)
                .content("10번째 댓글입니다.")
                .build();

        when(commentRepository.findById(expectedCommentId)).thenReturn(Optional.of(targetComment));

        //when
        Long result = commentService.modify(expectedCommentId, dto);

        //then
        assertThat(result).isEqualTo(expectedCommentId);
        assertThat(targetComment.getContent()).isEqualTo(expectedContent);
        assertThat(targetComment.isSecret()).isTrue();
    }

    @Test
    public void remove() {
        //given
        Long expectedCommentId = 10L;
        Comment targetComment = Comment.builder()
                .id(expectedCommentId)
                .content("10번째 댓글은 대댓글이 달려있어서 isDelete를 true로 변경합니다.")
                .build();
        Comment.builder().content("대댓글 달아줍니다.").build().setParent(targetComment);
        when(commentRepository.findById(expectedCommentId)).thenReturn(Optional.of(targetComment));

        //when
        Long result = commentService.remove(expectedCommentId);

        //then
        assertThat(result).isEqualTo(expectedCommentId);
        assertThat(targetComment.isDeleted()).isTrue();
    }

    @Test
    public void getList() throws Exception {
        //given
        User testUser = User.builder().id(3L).build();
        Post testPost = new Post(9L, null, testUser, "자유게시판", "9번 글입니다.^^");
        List<Comment> comments = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            LocalDateTime now = LocalDateTime.now();
            Comment parent = Comment.builder()
                    .post(testPost)
                    .user(testUser)
                    .content(i + "번째 댓글")
                    .createdDate(now)
                    .modifiedDate(now)
                    .build();
            comments.add(parent);
            if (i % 2 == 0) {
                continue; // 짝수번째 댓글에는 대댓글을 달지 않음
            }
            Thread.sleep(100);
            for (int j = 0; j < i; j++) { // 댓글의 순번만큼 반복
                LocalDateTime now2 = LocalDateTime.now();
                Comment child = Comment.builder()
                        .post(testPost)
                        .user(testUser)
                        .content(parent.getId() + "번 댓글의 대댓글: " + j)
                        .createdDate(now)
                        .modifiedDate(now2)
                        .build();
                child.setParent(parent);
                comments.add(child);
            }
        }
        when(commentRepository.findByPostId(testPost.getId())).thenReturn(comments);

        //when
        List<CommentListResponseDto> result = commentService.getList(testPost.getId(), testUser.getId());

        //then
        assertThat(result.size()).isEqualTo(comments.size());
        for (CommentListResponseDto dto : result) {
            System.out.printf("%s\t%s\tisModified: %b\tcontent: %s\n", dto.getCreatedDate(), dto.getModifiedDate(), dto.getIsModified(), dto.getContent());
        }
    }


}