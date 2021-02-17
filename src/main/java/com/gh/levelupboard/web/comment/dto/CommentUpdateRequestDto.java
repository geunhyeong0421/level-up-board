package com.gh.levelupboard.web.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentUpdateRequestDto {

    private Long id; // 댓글 번호
    private String content; // 내용
    private boolean isSecret; // 비밀 댓글 여부

}
