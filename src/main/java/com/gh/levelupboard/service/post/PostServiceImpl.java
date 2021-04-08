package com.gh.levelupboard.service.post;

import com.gh.levelupboard.config.auth.dto.SessionUser;
import com.gh.levelupboard.domain.board.Board;
import com.gh.levelupboard.domain.comment.CommentRepository;
import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.domain.post.PostRepository;
import com.gh.levelupboard.domain.user.User;
import com.gh.levelupboard.domain.user.UserRepository;
import com.gh.levelupboard.service.board.BoardService;
import com.gh.levelupboard.web.comment.dto.CommentListResponseDto;
import com.gh.levelupboard.web.pagination.Pagination;
import com.gh.levelupboard.web.post.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService{

    private final BoardService boardService;

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Long add(PostSaveRequestDto requestDto) {
        Board board = boardService.get(requestDto.getBoardId());

        Long userId = requestDto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이용자가 없습니다. id=" + userId));

        Long parentId = requestDto.getParentId();
        Post parent = (parentId != null)
                ? postRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + parentId))
                : null;

        Post post = requestDto.toEntity(board, user, parent);
        if (post.getGroupId() != null) { // 답글인 경우에만 실행
            postRepository.bulkGroupOrderPlus(post.getGroupId(), post.getGroupOrder());
        }
        return postRepository.save(post).getId();
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

        if (post.getChildren().size() != 0) { // 답글이 있으면 삭제 상태로 변경
            post.setDeleted();
        } else { // 답글이 없으면 DB에서 삭제
            delete(post);
            Post parent = post.getParent();

            // 원글이 삭제 상태인데 마지막 답글이 삭제됐다면
            while (parent != null && parent.isDeleted() && parent.getChildren().isEmpty()) {
                delete(parent);
                parent = parent.getParent();
            }
        }

        return id;
    }

    @Transactional // 조회수 변경 가능성
    @Override
    public PostResponseDto get(Long id, SessionUser loginUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        PostResponseDto postResponseDto = new PostResponseDto(post, loginUser);
        int commentsCount = post.getCommentsCount(); // 댓글 수

        int size = Pagination.COMMENT.getSize(); // 댓글 페이징 크기
        int lastPage = (commentsCount != 0) ? (int) Math.ceil(1.0 * commentsCount / size) : 1; // 마지막 페이지 계산
        PageRequest pageRequest = PageRequest.of(lastPage - 1, size); // 페이지의 인덱스를 넘겨준다

        Page<CommentListResponseDto> comments = commentRepository.findByPostIdWithPagination(id, pageRequest)
                .map(comment -> new CommentListResponseDto(comment, loginUser));
        postResponseDto.setComments(comments);

        return postResponseDto;
    }

    @Override
    public EditPostResponseDto getForEdit(Long id) {
        Post post = postRepository.findByIdForEdit(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return new EditPostResponseDto(post);
    }

    @Override
    public ReplyPostResponseDto getForReply(Long id) {
        Post post = postRepository.findByIdForReply(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return new ReplyPostResponseDto(post);
    }

    @Override
    public Page<PostListResponseDto> getList(Criteria cri) {
        return getList(null, cri);
    }

    @Override
    public Page<PostListResponseDto> getList(Long boardId, Criteria cri) {
        return postRepository.findByBoardIdWithPagination(boardId, cri)
                .map(PostListResponseDto::new);
    }






    private void delete(Post post) {
        if (post.getCommentsCount() != 0) { // 댓글이 있으면 on delete set null 반영
            commentRepository.bulkSetPostNull(post.getId()); // 벌크 연산으로 flush
        }
        postRepository.delete(post); // DB에서 삭제

        if (post.getGroupOrder() != 0) { // 벌크 연산으로 flush
            postRepository.bulkGroupOrderMinus(post.getGroupId(), post.getGroupOrder());
        }
    }

}
