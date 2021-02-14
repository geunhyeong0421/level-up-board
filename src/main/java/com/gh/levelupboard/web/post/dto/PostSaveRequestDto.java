package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto { // 게시글 등록 요청 정보

    private Long userId; // 작성자(로그인 유저)
    private String title; // 제목
    private String content; // 내용


    public PostSaveRequestDto(Long userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    public Post toEntity(User user) {
        return new Post(user, title, content);
    }
}
