package com.gh.levelupboard.service.comment;

import com.gh.levelupboard.config.auth.dto.SessionUser;
import com.gh.levelupboard.web.comment.dto.CommentListResponseDto;
import com.gh.levelupboard.web.comment.dto.CommentResultDto;
import com.gh.levelupboard.web.comment.dto.CommentSaveRequestDto;
import com.gh.levelupboard.web.comment.dto.CommentUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface CommentService {

    // 댓글 등록
    CommentResultDto add(CommentSaveRequestDto requestDto);

    // 댓글 수정
    CommentResultDto modify(Long id, CommentUpdateRequestDto requestDto);

    // 댓글 삭제
    CommentResultDto remove(Long id);

    // 댓글 목록 조회
//    List<CommentListResponseDto> getList(Long postId, SessionUser loginUser);

    // 댓글 목록 조회 + 페이징
    Page<CommentListResponseDto> getListWithPagination(Long postId, SessionUser loginUser, Pageable pageable);

}
