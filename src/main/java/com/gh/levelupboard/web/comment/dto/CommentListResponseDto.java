package com.gh.levelupboard.web.comment.dto;

import com.gh.levelupboard.domain.comment.Comment;
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
    private String modifiedDate; // 최종 수정일
    private boolean isModified; // 수정 여부

    private String content; // 내용
    private boolean isSecret; // 비밀 여부
    private boolean isDeleted; // 삭제 여부

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


    public CommentListResponseDto(Comment entity, Long loginUserId) {
        isMyComment = entity.getUser().getId().equals(loginUserId);

        id = entity.getId();
        Comment parent = entity.getParent();
        if (parent != null) {
            parentId = parent.getId();
        }

        profile = entity.getUser().getPicture();
        writer = entity.getUser().getName();

        LocalDateTime modifiedDate = entity.getModifiedDate(); // 수정일만 꺼내서 쓰고
        isModified = !modifiedDate.isEqual(entity.getCreatedDate()); // 작성일과 비교
        this.modifiedDate = pattern(modifiedDate);

        content = entity.getContent();
        isSecret = entity.isSecret();
        isDeleted = entity.isDeleted();
    }

    private String pattern(LocalDateTime dateTime) {
        if (dateTime.toLocalDate().equals(LocalDate.now())) { // 당일 작성된 글이면
            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm"));
    }




}
