package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.config.auth.dto.SessionUser;
import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.web.pagination.Pagination;
import com.gh.levelupboard.web.pagination.PaginationDto;
import com.gh.levelupboard.web.comment.dto.CommentListResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PostResponseDto { // 게시글 조회 화면에 사용

    private boolean isMyPost; // 조회 게시글의 본인 작성 여부

    private boolean canReply; // 답글 가능 여부
    private String boardName; // 게시판 이름
    private Long id; // 번호
    private String title; // 제목
    private String content; // 내용

    private String profile; // 작성자 프로필 사진
    private String writer; // 작성자 이름
    private int hit; // 조회수
    private int commentCount; // 댓글수

    private String createdDate; // 작성일
    private String modifiedDate; // 최종 수정일

    private boolean isModified; // 수정 여부

    private List<CommentListResponseDto> comments; // 댓글 목록
    private PaginationDto pageMaker;

    public PostResponseDto(Post entity, SessionUser loginUser) {
        isMyPost = entity.isMyPost(loginUser.getId()); // 조건에 따라 조회수 +1

        canReply = entity.getBoard().getCreatePermission() == null || loginUser.isAdmin();
        boardName = entity.getBoard().getName();
        id = entity.getId();
        title = entity.getTitle();
        content = entity.getContent();

        profile = entity.getUser().getPicture();
        writer = entity.getUser().getName();
        hit = entity.getHit(); // 변동된 조회수 적용
        commentCount = entity.getCommentsCount();

        LocalDateTime createdDate = entity.getCreatedDate();
        LocalDateTime modifiedDate = entity.getModifiedDate();
        isModified = !createdDate.equals(modifiedDate);

        this.createdDate = pattern(createdDate);
        this.modifiedDate = pattern(modifiedDate);
    }

    private String pattern(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm");
        return dateTime.format(formatter);
    }

    // 최신 댓글(마지막 페이지) 출력
    public void setComments(Page<CommentListResponseDto> pageResult) {
        comments = pageResult.getContent();
        pageMaker = new PaginationDto(pageResult, Pagination.COMMENT.getNavSize());
    }


}