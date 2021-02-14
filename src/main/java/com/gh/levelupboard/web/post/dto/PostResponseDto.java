package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.domain.post.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;

    private Long userId; // 작성자 ID(본인 게시글 확인용)
    private String profile; // 작성자 프로필 사진
    private String writer; // 작성자 이름
    private int hit; // 게시글 조회수

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public PostResponseDto(Post entity) {
        id = entity.getId();
        title = entity.getTitle();
        content = entity.getContent();

        userId = entity.getUser().getId();
        profile = entity.getUser().getPicture();
        writer = entity.getUser().getName();

        createdDate = entity.getCreatedDate();
        modifiedDate = entity.getModifiedDate();
    }
}
