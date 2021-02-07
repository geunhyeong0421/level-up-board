package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.domain.post.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private Long userId;
    private String writer;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public PostResponseDto(Post entity) {
        id = entity.getId();
        title = entity.getTitle();
        content = entity.getContent();
        userId = entity.getUser().getId();
        writer = entity.getUser().getName();
        createdDate = entity.getCreatedDate();
        modifiedDate = entity.getModifiedDate();
    }
}
