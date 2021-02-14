package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

    private Long userId;
    private String title;
    private String content;


    public PostSaveRequestDto(Long userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    public Post toEntity(User user) {
        return new Post(user, title, content);
    }
}
