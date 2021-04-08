package com.gh.levelupboard.web.board.dto;

import com.gh.levelupboard.domain.board.Board;
import com.gh.levelupboard.domain.user.Role;

public class BoardListDto {

    private Long id;
    private String name;
    private Role createPermission;
    private boolean adminOnly;

    private boolean isCurrentBoard;

    public BoardListDto(Board board, Long boardId) {
        id = board.getId();
        name = board.getName();
        createPermission = board.getCreatePermission();
        adminOnly = createPermission.equals(Role.ADMIN);

        isCurrentBoard = id.equals(boardId);
    }

}
