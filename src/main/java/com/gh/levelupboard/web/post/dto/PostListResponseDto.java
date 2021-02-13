package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.domain.post.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostListResponseDto {
    private Long id;
    private String title;
    private String writer;
    private LocalDateTime createdDate;
    private int hit;

    public PostListResponseDto(Post entity) {
        id = entity.getId();
        title = entity.getTitle();
        writer = entity.getUser().getName();
        createdDate = entity.getCreatedDate();
        hit = 0;
    }
}
