package com.gh.levelupboard.web.post.dto;


import com.gh.levelupboard.domain.post.Post;

public class EditPostResponseDto { // 게시글 수정 화면에 사용

    private Long id; // 번호
    private String title; // 제목
    private String content; // 내용

    public EditPostResponseDto(Post entity) {
        id = entity.getId();
        title = entity.getTitle();
        content = entity.getContent();
    }
}
