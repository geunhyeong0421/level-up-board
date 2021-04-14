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

@Transactional
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public CommentResultDto add(CommentSaveRequestDto requestDto) {
        Long postId = requestDto.getPostId();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postId));

        Long userId = requestDto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이용자가 없습니다. id=" + userId));

        Long parentId = requestDto.getParentId();
        Comment parent = (parentId != null)
                ? commentRepository.findById(parentId)
                   .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + parentId))
                : null;

        Long commentId = commentRepository.save(requestDto.toEntity(post, user, parent)).getId();
        int commentRownum = commentRepository.findIdByPostId(postId).indexOf(commentId) + 1;

        return new CommentResultDto(commentId, commentRownum);
    }

    @Override
    public CommentResultDto modify(Long id, CommentUpdateRequestDto requestDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + id));

        comment.update(requestDto.getContent(), requestDto.getIsSecret());

        int rownum = commentRepository.findIdByPostId(comment.getPost().getId()).indexOf(id) + 1;
        return new CommentResultDto(id ,rownum);
    }

    @Override
    public CommentResultDto remove(Long id) {
        Comment comment = commentRepository.findById(id) // 성능 최적화 쿼리 필요
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + id));

        List<Long> commentIdList = commentRepository.findIdByPostId(comment.getPost().getId());
        if (comment.getChildren().isEmpty()) { // 답글이 없으면
            delete(comment);

            Comment parent = comment.getParent();
            // 게시글의 경우와 같지만 JPA의 쓰기 지연으로 DB에 delete 쿼리가 나가지 않았음을 감안해서 로직을 작성
            while (parent != null && parent.isDeleted() && parent.getChildren().size() == 1) {
                delete(parent);
                commentIdList.remove(parent.getId()); // 목록에서 제거

                parent = parent.getParent();
            }

            Long targetId = null;
            int targetRownum = 0;
            if (commentIdList.size() != 1) { // 화면에 출력할 댓글이 존재(계산을 위해 삭제된 댓글을 남겨뒀으므로 0이 아닌 1이 기준)
                int deletedCommentIndex = commentIdList.indexOf(id); // 삭제된 댓글의 index
                targetId = (deletedCommentIndex == commentIdList.size() - 1) // 마지막 댓글이 삭제된 경우
                        ? commentIdList.get(deletedCommentIndex - 1) // 바로 앞의 댓글을 추적
                        : commentIdList.get(deletedCommentIndex + 1); // 아니면 바로 뒤의 댓글을 추적
                commentIdList.remove(deletedCommentIndex); // 삭제된 댓글을 목록에서 제거하고 화면에 출력할 댓글들만 남긴다
                targetRownum = commentIdList.indexOf(targetId) + 1; // 추적 댓글의 rownum을 계산
            }

            return new CommentResultDto(targetId, targetRownum);
        } else { // 답글이 있으면
            comment.setDeleted(); // 삭제 상태로 변경

            return new CommentResultDto(id, commentIdList.indexOf(id) + 1);
        }
    }


//    @Transactional(readOnly = true)
//    @Override
//    public List<CommentListResponseDto> getList(Long postId, SessionUser loginUser) {
//        return commentRepository.findByPostId(postId).stream()
//                .map(comment -> new CommentListResponseDto(comment, loginUser))
//                .collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    @Override
    public Page<CommentListResponseDto> getListWithPagination(Long postId, SessionUser loginUser, Pageable pageable) {
        return commentRepository.findByPostIdWithPagination(postId, pageable)
                .map(comment -> new CommentListResponseDto(comment, loginUser));
    }


    private void delete(Comment comment) {
        commentRepository.delete(comment); // DB에서 삭제
        comment.getPost().decreaseCommentsCount(); // 댓글수 -1
    }

}
