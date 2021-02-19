package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.domain.post.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PostListResponseDto { // 게시글 목록 출력에 사용

    private Long id; // 번호
    private String title; // 제목
    private String writer; // 작성자
    private String createdDate; // 작성일
    private int hit; // 조회수
    private int commentCount; // 댓글수


    public PostListResponseDto(Post entity) {
        id = entity.getId();
        title = entity.getTitle();
        writer = entity.getUser().getName();
        createdDate = pattern(entity.getCreatedDate());
        hit = entity.getHit();
        commentCount = entity.getCommentCount();
    }

    private String pattern(LocalDateTime dateTime) {
        if (dateTime.toLocalDate().equals(LocalDate.now())) { // 당일 작성된 글이면
            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd."));
    }

}
