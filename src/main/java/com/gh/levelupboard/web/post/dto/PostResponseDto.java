package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.domain.post.Post;
import lombok.Getter;

import java.time.LocalDateTime;

//@Getter
public class PostResponseDto { // 게시글 조회 화면에 사용

    private Long id; // 번호
    private String title; // 제목
    private String content; // 내용

    private String profile; // 작성자 프로필 사진
    private String writer; // 작성자 이름
    private int hit; // 조회수

    private LocalDateTime createdDate; // 작성일
    private LocalDateTime modifiedDate; // 최종 수정일

    private boolean isMyPost; // 조회 게시글의 본인 작성 여부


    public PostResponseDto(Post entity, boolean isMyPost) {
        id = entity.getId();
        title = entity.getTitle();
        content = entity.getContent();

        profile = entity.getUser().getPicture();
        writer = entity.getUser().getName();
        hit = entity.getHit();

        createdDate = entity.getCreatedDate();
        modifiedDate = entity.getModifiedDate();

        this.isMyPost = isMyPost;
    }
}
