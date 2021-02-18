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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Long add(CommentSaveRequestDto requestDto) {
        Long postId = requestDto.getPostId();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postId));
        Long userId = requestDto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이용자가 없습니다. id=" + userId));
        Long parentId = requestDto.getParentId();
        Comment parent = null;
        if (parentId != null) {
            parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + parentId));
        }

        return commentRepository.save(requestDto.toEntity(post, user, parent)).getId();
    }

    @Transactional
    @Override
    public Long modify(Long id, CommentUpdateRequestDto requestDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + id));

        comment.update(requestDto.getContent(), requestDto.getIsSecret());
        return id;
    }

    @Transactional
    @Override
    public Long remove(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + id));

        if (comment.getChildren().size() != 0) { // 대댓글이 있으면 삭제 상태로 변경
            comment.delete();
        } else {
            comment.getPost().decreaseCommentCount(); // 댓글수 -1
            commentRepository.delete(comment); // 대댓글이 없으면 DB에서 삭제
        }
        return id;
    }

    @Override
    public List<CommentListResponseDto> getList(Long postId, Long loginUserId) {
        return commentRepository.findByPostId(postId).stream()
                .map(comment -> new CommentListResponseDto(comment, loginUserId))
                .collect(Collectors.toList());
    }

}
