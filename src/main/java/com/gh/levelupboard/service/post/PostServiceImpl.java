package com.gh.levelupboard.service.post;

import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.domain.post.PostRepository;
import com.gh.levelupboard.domain.user.User;
import com.gh.levelupboard.domain.user.UserRepository;
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
        postRepository.delete(post);
        return id;
    }

    @Transactional
    @Override
    public PostResponseDto get(Long postId, Long longinUserId) {
        Post post = postRepository.findByIdFetch(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postId));

        // 게시물 작성자와 이용자(로그인 유저) 일치 여부를 확인
        boolean isMyPost = post.getUser().getId().equals(longinUserId);
        if (isMyPost == false) { // 본인 글이 아니면 조회수 +1
            post.increaseHit();
        }
        return new PostResponseDto(post, isMyPost);
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
