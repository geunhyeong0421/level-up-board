package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.config.auth.dto.SessionUser;
import com.gh.levelupboard.domain.board.Board;
import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.domain.user.Role;
import com.gh.levelupboard.domain.user.User;
import com.gh.levelupboard.web.comment.dto.CommentListResponseDto;
import com.gh.levelupboard.web.pagination.Pagination;
import com.gh.levelupboard.web.pagination.PaginationDto;
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

    private String profilePicture; // 작성자 프로필 사진
    private String writerName; // 작성자 이름
    private int hit; // 조회수
    private int commentsCount; // 댓글수

    private String createdDate; // 작성일
    private String modifiedDate; // 최종 수정일

    private boolean isModified; // 수정 여부

    private List<CommentListResponseDto> comments; // 댓글 목록
    private PaginationDto pageMaker;

    public PostResponseDto(Post entity, SessionUser loginUser) {
        User writer = entity.getUser();
        isMyPost = writer.getId().equals(loginUser.getId());
        if (!isMyPost) { // 내 게시물이 아니면 조회수 + 1
            entity.increaseHit();
        }
        profilePicture = writer.getPicture();
        writerName = writer.getName();

        Board currentBoard = entity.getBoard();
        canReply = currentBoard.getCreatePermission().equals(Role.USER) || loginUser.isAdmin();
        boardName = currentBoard.getName();

        id = entity.getId();
        title = entity.getTitle();
        content = entity.getContent();
        hit = entity.getHit(); // 변동된 조회수 적용
        commentsCount = entity.getCommentsCount();

        LocalDateTime createdDate = entity.getCreatedDate();
        LocalDateTime modifiedDate = entity.getModifiedDate();
        isModified = !createdDate.equals(modifiedDate);

        this.createdDate = pattern(createdDate);
        this.modifiedDate = pattern(modifiedDate);
    }

    private String pattern(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm"));
    }

    // 최신 댓글(마지막 페이지) 출력
    public void setComments(Page<CommentListResponseDto> pageResult) {
        comments = pageResult.getContent();
        pageMaker = new PaginationDto(pageResult, Pagination.COMMENT.getNavSize());
    }


}