package com.gh.levelupboard.web.comment;

import com.gh.levelupboard.config.auth.LoginUser;
import com.gh.levelupboard.config.auth.dto.SessionUser;
import com.gh.levelupboard.service.comment.CommentService;
import com.gh.levelupboard.web.comment.dto.CommentListResponseDto;
import com.gh.levelupboard.web.comment.dto.CommentSaveRequestDto;
import com.gh.levelupboard.web.comment.dto.CommentUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentService commentService;


    // 등록
    @PostMapping("/api/v1/comments")
    public Long addComment(@LoginUser SessionUser user, @RequestBody CommentSaveRequestDto requestDto) {
        requestDto.setUserId(user.getId());
        return commentService.add(requestDto);
    }

    // 수정
    @PutMapping("/api/v1/comments/{id}")
    public Long modifyComment(@PathVariable Long id, @RequestBody CommentUpdateRequestDto requestDto) {
        return commentService.modify(id, requestDto);
    }

    // 삭제
    @DeleteMapping("/api/v1/comments/{id}")
    public Long removeComment(@PathVariable Long id) {
        return commentService.remove(id);
    }

    // 조회
    @GetMapping("/api/v1/posts/{postId}/comments")
    public List<CommentListResponseDto> getCommentList(@LoginUser SessionUser user, @PathVariable Long postId) {
        return commentService.getList(postId, user.getId());
    }

}
