package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.domain.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

    private String title;
    private String content;
    private String writer;

    public PostSaveRequestDto(String title, String content, String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    public Post toEntity() {
        return new Post(title, content, writer);
    }
}
