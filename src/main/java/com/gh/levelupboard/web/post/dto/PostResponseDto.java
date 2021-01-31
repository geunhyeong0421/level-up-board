package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.domain.post.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdDate;

    public PostResponseDto(Post entity) {
        id = entity.getId();
        title = entity.getTitle();
        content = entity.getContent();
        writer = entity.getWriter();
        createdDate = entity.getCreatedDate();
    }
}
