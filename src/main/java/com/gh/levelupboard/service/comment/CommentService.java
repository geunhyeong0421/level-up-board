package com.gh.levelupboard.service.comment;

import com.gh.levelupboard.config.auth.dto.SessionUser;
import com.gh.levelupboard.web.comment.dto.CommentListResponseDto;
import com.gh.levelupboard.web.comment.dto.CommentSaveRequestDto;
import com.gh.levelupboard.web.comment.dto.CommentUpdateRequestDto;

import java.util.List;


public interface CommentService {

    // 댓글 등록
    Long add(CommentSaveRequestDto requestDto);

    // 댓글 수정
    Long modify(Long id, CommentUpdateRequestDto requestDto);

    // 댓글 삭제
    Long remove(Long id);

    // 댓글 목록 조회
    List<CommentListResponseDto> getList(Long postId, SessionUser loginUser);

}
