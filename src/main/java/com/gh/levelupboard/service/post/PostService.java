package com.gh.levelupboard.service.post;

import com.gh.levelupboard.config.auth.dto.SessionUser;
import com.gh.levelupboard.web.post.dto.*;

import java.util.List;

public interface PostService {

    // 게시글 등록
    Long add(PostSaveRequestDto requestDto);

    // 게시글 수정
    Long modify(Long id, PostUpdateRequestDto requestDto);

    // 게시글 삭제
    Long remove(Long id);

    // 게시글 조회
    PostResponseDto get(Long id, SessionUser loginUser);

    // 게시글 수정을 위한 정보만을 조회
    EditPostResponseDto getForEdit(Long id);

    // 답글 작성을 위한 정보만을 조회
    ReplyPostResponseDto getForReply(Long id);

    // 전체글 조회
    List<PostListResponseDto> getListDesc();

}
