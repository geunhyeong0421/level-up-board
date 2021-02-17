package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.web.comment.dto.CommentListResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDto { // 게시글 조회 화면에 사용

    private boolean isMyPost; // 조회 게시글의 본인 작성 여부

    private Long id; // 번호
    private String title; // 제목
    private String content; // 내용

    private String profile; // 작성자 프로필 사진
    private String writer; // 작성자 이름
    private int hit; // 조회수
    private int commentCount; // 댓글수

    private LocalDateTime createdDate; // 작성일
    private LocalDateTime modifiedDate; // 최종 수정일

    private List<CommentListResponseDto> comments; // 댓글 목록


    public PostResponseDto(Post entity, Long longinUserId) {
        isMyPost = entity.isMyPost(longinUserId); // 조건에 따라 조회수 +1

        id = entity.getId();
        title = entity.getTitle();
        content = entity.getContent();

        profile = entity.getUser().getPicture();
        writer = entity.getUser().getName();
        hit = entity.getHit(); // 변동된 조회수 적용
        commentCount = entity.getCommentCount();

        createdDate = entity.getCreatedDate();
        modifiedDate = entity.getModifiedDate();
    }
}
