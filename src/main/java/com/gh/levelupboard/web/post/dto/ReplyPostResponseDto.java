package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.domain.user.Role;
import lombok.Getter;

@Getter
public class ReplyPostResponseDto {

    private Long boardId; // 게시판 id
    private String boardName; // 게시판 이름
    private Role createPermission;

    private Long id; // 번호
    private String title; // 제목

    public ReplyPostResponseDto(Post entity) {
        boardId = entity.getBoard().getId();
        boardName = entity.getBoard().getName();
        createPermission = entity.getBoard().getCreatePermission();

        id = entity.getId();
        title = entity.getTitle();
    }

}
