package com.gh.levelupboard.web.comment.dto;

import com.gh.levelupboard.config.auth.dto.SessionUser;
import com.gh.levelupboard.domain.comment.Comment;
import com.gh.levelupboard.domain.user.Role;
import com.gh.levelupboard.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class CommentListResponseDto {

    private boolean isMyComment; // 본인 댓글 여부

    private Long id; // 댓글 번호
    private Long parentId; // 부모 댓글 번호

    private String profile; // 작성자 프로필 사진
    private String writer; // 작성자 이름
    private boolean equalsPostWriter; // 댓글 작성자 equals 게시글 작성자
    private String modifiedDate; // 최종 수정일
    private boolean isModified; // 수정 여부

    private String replyTo; // 답글 수신자 이름
    private String content; // 내용
    private boolean isSecret; // 비밀 여부
    private boolean isDeleted; // 삭제 여부

    private boolean isVisible; // 비밀 여부에 따른 가시성(댓글 작성자, 게시글 작성자, 답글 수신자, 관리자)

    // json 파싱 시에 boolean 타입의 key값에 'is'가 생략되는 문제를 해결
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
        isVisible = !isSecret; // 가시성은 비밀 여부의 반대

        User postWriter = entity.getPost().getUser();
        User commentWriter = entity.getUser();
        equalsPostWriter = postWriter.equals(commentWriter);

        isMyComment = commentWriter.getId().equals(loginUser.getId());
        boolean isAdmin = loginUser.getRole().equals(Role.ADMIN);

        id = entity.getId();
        Comment parent = entity.getParent();
        if (parent == null) { // 답글이 아닌 댓글이면
            if (isSecret) {
                boolean isPostWriter = postWriter.getId().equals(loginUser.getId());
                isVisible = isMyComment || isPostWriter || isAdmin;
            }
        } else { // 답글이면
            parentId = parent.getId();
            if (isSecret) {
                boolean isReplyTo = parent.getUser().getId().equals(loginUser.getId());
                isVisible = isMyComment || isReplyTo || isAdmin;
            }
            if (parentId != parent.getGroupId()) { // 답글의 답글이면
                replyTo = parent.getUser().getName();
            }
        }

        profile = commentWriter.getPicture();
        writer = commentWriter.getName();

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
