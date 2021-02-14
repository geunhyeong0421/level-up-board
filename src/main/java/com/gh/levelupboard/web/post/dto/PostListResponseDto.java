package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.domain.post.Post;
import lombok.Getter;

import java.time.LocalDateTime;

//@Getter
public class PostListResponseDto { // 게시글 목록 출력에 사용

    private Long id; // 번호
    private String title; // 제목
    private String writer; // 작성자
    private LocalDateTime createdDate; // 작성일
    private int hit; // 조회수


    public PostListResponseDto(Post entity) {
        id = entity.getId();
        title = entity.getTitle();
        writer = entity.getUser().getName();
        createdDate = entity.getCreatedDate();
        hit = entity.getHit();
    }
}
