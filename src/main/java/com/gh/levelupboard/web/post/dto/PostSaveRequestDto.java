package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

    private String title;
    private String content;
    private Long userId;

    public PostSaveRequestDto(String title, String content, Long userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    public Post toEntity(User user) {
        return new Post(title, content, user);
    }
}
