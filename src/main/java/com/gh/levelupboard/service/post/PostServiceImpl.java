package com.gh.levelupboard.service.post;

import com.gh.levelupboard.config.auth.dto.SessionUser;
import com.gh.levelupboard.domain.comment.CommentRepository;
import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.domain.post.PostRepository;
import com.gh.levelupboard.domain.user.User;
import com.gh.levelupboard.domain.user.UserRepository;
import com.gh.levelupboard.web.comment.dto.CommentListResponseDto;
import com.gh.levelupboard.web.post.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Long add(PostSaveRequestDto requestDto) {
        Long userId = requestDto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이용자가 없습니다. id=" + userId));
        return postRepository.save(requestDto.toEntity(user)).getId();
    }

    @Transactional
    @Override
    public Long modify(Long id, PostUpdateRequestDto requestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        post.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    @Transactional
    @Override
    public Long remove(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        if (post.getCommentCount() != 0) { // 댓글이 있으면 삭제 상태로 변경
            post.delete();
        } else { // 댓글이 없으면 DB에서 삭제
            postRepository.delete(post);
        }
        return id;
    }

    @Transactional // 조회수 변경 가능성
    @Override
    public PostResponseDto get(Long postId, SessionUser loginUser) {
        Post post = postRepository.findByIdFetch(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postId));
        PostResponseDto postResponseDto = new PostResponseDto(post, loginUser.getId());

        List<CommentListResponseDto> comments = commentRepository.findByPostId(postId).stream()
                .map(comment -> new CommentListResponseDto(comment, loginUser))
                .collect(Collectors.toList());
        postResponseDto.setComments(comments);

        return postResponseDto;
    }

    @Override
    public EditPostResponseDto getForEdit(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return new EditPostResponseDto(post);
    }

    @Override
    public List<PostListResponseDto> getListDesc() {
        return postRepository.findAllDesc().stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

}
