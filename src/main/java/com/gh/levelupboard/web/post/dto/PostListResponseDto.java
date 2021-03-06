package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.domain.post.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PostListResponseDto { // 게시글 목록 출력에 사용

    private String boardName; // 게시판 이름

    private Long id; // 번호
    private String title; // 제목
    private int commentsCount; // 댓글수

    private String writerName; // 작성자

    private String createdDate; // 작성일
    private int hit; // 조회수

    private boolean isDeleted;

    private List<Object> depth = new ArrayList<>();


    public PostListResponseDto(Post entity) {
        boardName = entity.getBoard().getName();

        id = entity.getId();
        title = entity.getTitle();
        commentsCount = entity.getCommentsCount();

        writerName = entity.getUser().getName();

        createdDate = pattern(entity.getCreatedDate());
        hit = entity.getHit();

        isDeleted = entity.isDeleted();

        for (int i = 0; i < entity.getDepth(); i++) {
            depth.add(new Object());
        }
    }

    private String pattern(LocalDateTime dateTime) {
        if (dateTime.toLocalDate().equals(LocalDate.now())) { // 당일 작성된 글이면
            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd."));
    }

}
