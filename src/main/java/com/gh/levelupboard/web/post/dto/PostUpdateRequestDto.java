package com.gh.levelupboard.web.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequestDto {
    private String title;
    private String content;

    public PostUpdateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
