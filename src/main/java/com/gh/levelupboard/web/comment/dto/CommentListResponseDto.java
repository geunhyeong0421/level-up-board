package com.gh.levelupboard.web.comment.dto;

import com.gh.levelupboard.config.auth.dto.SessionUser;
import com.gh.levelupboard.domain.comment.Comment;
import com.gh.levelupboard.domain.user.User;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CommentListResponseDto {

    private boolean isMyComment; // 본인 댓글 여부

    private Long id; // 댓글 번호
    private Long parentId; // 부모 댓글 번호
    private Long groupId;

    private String profilePicture; // 작성자 프로필 사진
    private String writerName; // 작성자 이름
    private boolean equalsPostWriter; // 댓글 작성자 equals 게시글 작성자
    private String modifiedDate; // 최종 수정일
    private boolean isModified; // 수정 여부

    private String replyTo; // 답글 수신자 이름
    private String content; // 내용
    private boolean isSecret; // 비밀 여부
    private boolean isDeleted; // 삭제 여부

    private boolean isVisible; // 비밀 댓글 공개 여부

    public boolean getIsMyComment() {
        return isMyComment;
    }
    public boolean getIsModified() {
        return isModified;
    }
    public boolean getIsSecret() {
        return isSecret;
    }
    public boolean getIsDeleted() {
        return isDeleted;
    }
    public boolean getIsVisible() {
        return isVisible;
    }

    public CommentListResponseDto(Comment entity, SessionUser loginUser) {
        isSecret = entity.isSecret();
        isVisible = !isSecret; // 비밀 여부에 따른 공개 여부

        User commentWriter = entity.getUser();
        User postWriter = entity.getPost().getUser();
        equalsPostWriter = commentWriter.equals(postWriter);

        isMyComment = commentWriter.getId().equals(loginUser.getId());
        if (!isVisible) { // 댓글 작성자, 게시글 작성자, 관리자에게는 비밀 댓글 공개
            boolean isPostWriter = postWriter.getId().equals(loginUser.getId());
            isVisible = isMyComment || isPostWriter || loginUser.isAdmin();
        }

        id = entity.getId();
        Comment parent = entity.getParent();
        if (parent != null) { // 답글(대댓글)이면
            parentId = parent.getId();
            if (parentId != parent.getGroupId()) { // 답글의 답글이면
                replyTo = parent.getUser().getName();
            }
            if (!isVisible) { // 답글의 수신자에게는 공개
                isVisible = parent.getUser().getId().equals(loginUser.getId());
            }
        }
        groupId = entity.getGroupId();

        profilePicture = commentWriter.getPicture();
        writerName = commentWriter.getName();

        LocalDateTime modifiedDate = entity.getModifiedDate(); // 수정일만 꺼내서 쓰고
        isModified = !modifiedDate.isEqual(entity.getCreatedDate()); // 작성일과 비교
        this.modifiedDate = pattern(modifiedDate);

        content = entity.getContent();
        isDeleted = entity.isDeleted();
    }

    private String pattern(LocalDateTime dateTime) {
        if (dateTime.toLocalDate().equals(LocalDate.now())) { // 당일 작성된 글이면
            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm"));
    }


}
