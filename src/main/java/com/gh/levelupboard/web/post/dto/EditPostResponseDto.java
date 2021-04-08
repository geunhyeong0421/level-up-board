package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.domain.post.Post;
import lombok.Getter;

@Getter
public class EditPostResponseDto { // 게시글 수정 [화면]에 사용

    private String boardName; // 게시판 이름

    private Long id; // 번호
    private String title; // 제목
    private String content; // 내용

    private Long writerId; // 작성자 id

    public EditPostResponseDto(Post entity) {
        boardName = entity.getBoard().getName();

        id = entity.getId();
        title = entity.getTitle();
        content = entity.getContent();

        writerId = entity.getUser().getId();
    }

}
