package com.gh.levelupboard.service.comment;

import com.gh.levelupboard.config.auth.dto.SessionUser;
import com.gh.levelupboard.domain.comment.Comment;
import com.gh.levelupboard.domain.comment.CommentRepository;
import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.domain.post.PostRepository;
import com.gh.levelupboard.domain.user.User;
import com.gh.levelupboard.domain.user.UserRepository;
import com.gh.levelupboard.web.comment.dto.CommentListResponseDto;
import com.gh.levelupboard.web.comment.dto.CommentResultDto;
import com.gh.levelupboard.web.comment.dto.CommentSaveRequestDto;
import com.gh.levelupboard.web.comment.dto.CommentUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public CommentResultDto add(CommentSaveRequestDto requestDto) {
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
        Long commentId = commentRepository.save(requestDto.toEntity(post, user, parent)).getId();
        int commentRownum = commentRepository.findIdByPostId(postId).indexOf(commentId) + 1;
        return new CommentResultDto(commentId, commentRownum);
    }

    @Transactional
    @Override
    public CommentResultDto modify(Long id, CommentUpdateRequestDto requestDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + id));

        comment.update(requestDto.getContent(), requestDto.getIsSecret());

        int rownum = commentRepository.findIdByPostId(comment.getPost().getId()).indexOf(id) + 1;
        return new CommentResultDto(id ,rownum);
    }

    @Transactional
    @Override
    public CommentResultDto remove(Long id) {
        Comment comment = commentRepository.findById(id) // 성능 최적화 쿼리 필요
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + id));

        int rownum = commentRepository.findIdByPostId(comment.getPost().getId()).indexOf(id) + 1;
        if (comment.getChildren().size() != 0) { // 답글이 있으면 삭제 상태로 변경
            comment.delete();
            return new CommentResultDto(id, rownum);
        } else {
            commentRepository.delete(comment); // 답글이 없으면 DB에서 삭제
            comment.getPost().decreaseCommentCount(); // 댓글수 -1
            rownum--;

            Comment parent = comment.getParent();
            // 부모 댓글이 삭제 상태인데 마지막 답글이 삭제됐다면
            while (parent != null && parent.isDeleted() && parent.getChildren().size() == 1) {
                commentRepository.delete(parent); // 부모 댓글을 DB에서 삭제
                parent.getPost().decreaseCommentCount(); // 댓글수 -1
                rownum--;

                parent = parent.getParent();
            }
        }

        return new CommentResultDto(null, rownum);
    }

    @Override
    public List<CommentListResponseDto> getList(Long postId, SessionUser loginUser) {
        return commentRepository.findByPostId(postId).stream()
                .map(comment -> new CommentListResponseDto(comment, loginUser))
                .collect(Collectors.toList());
    }

    @Override
    public Page<CommentListResponseDto> getListWithPagination(Long postId, SessionUser loginUser, Pageable pageable) {
        return commentRepository.findByPostIdWithPagination(postId, pageable)
                .map(comment -> new CommentListResponseDto(comment, loginUser));
    }

}
