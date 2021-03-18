package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.web.pagination.Pagination;
import com.gh.levelupboard.web.pagination.PaginationDto;
import com.gh.levelupboard.web.comment.dto.CommentListResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    private String createdDate; // 작성일
    private String modifiedDate; // 최종 수정일

    private boolean isModified; // 수정 여부

    private List<CommentListResponseDto> comments; // 댓글 목록
    private boolean commentsFirst;
    private Integer commentsPrev;
    private List<PaginationDto> commentsPages = new ArrayList<>();

    public PostResponseDto(Post entity, Long longinUserId) {
        isMyPost = entity.isMyPost(longinUserId); // 조건에 따라 조회수 +1

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
    public void setComments(Page<CommentListResponseDto> comments) {
        int totalPages = comments.getTotalPages(); // 전체 페이지 수
        if (totalPages == 0) { return; }

        this.comments = comments.getContent();

        int paginationNavSize = Pagination.COMMENT.getNavSize(); // 화면에 출력되는 탐색 페이지 수
        int totalNavPages = (int) Math.ceil(1.0 * totalPages / paginationNavSize);
        int startPage = (totalNavPages - 1) * paginationNavSize + 1;

        commentsFirst = comments.isFirst(); // 첫 페이지 여부
        if (totalNavPages > 1) {
            commentsPrev = startPage - 1;
        }

        for (int i = startPage; i <= totalPages; i++) {
            commentsPages.add(new PaginationDto(i, i == totalPages));
        }
    }

}