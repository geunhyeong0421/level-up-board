package com.gh.levelupboard.web.comment.dto;

import com.gh.levelupboard.domain.comment.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentListResponseDto {

    private Long id; // 댓글 번호
    private Long parentId; // 부모 댓글 번호

    private String profile; // 작성자 프로필 사진
    private String writer; // 작성자 이름
    private LocalDateTime createdDate; // 작성일
    private LocalDateTime modifiedDate; // 최종 수정일
    private boolean isModified; // 수정 여부

    private String content; // 내용
    private boolean isSecret; // 비밀 여부
    private boolean isDeleted; // 삭제 여부

    private boolean isMyComment; // 본인 댓글 여부


    public CommentListResponseDto(Comment entity, Long loginUserId) {
        id = entity.getId();
        Comment parent = entity.getParent();
        if (parent != null) {
            parentId = parent.getId();
        }

        profile = entity.getUser().getPicture();
        writer = entity.getUser().getName();
        createdDate = entity.getCreatedDate();
        modifiedDate = entity.getModifiedDate();
        isModified = !createdDate.isEqual(modifiedDate);

        content = entity.getContent();
        isSecret = entity.isSecret();
        isDeleted = entity.isDeleted();

        isMyComment = entity.getUser().getId().equals(loginUserId);
    }


}
