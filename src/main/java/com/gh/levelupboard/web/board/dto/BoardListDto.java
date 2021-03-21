package com.gh.levelupboard.web.board.dto;

import com.gh.levelupboard.domain.board.Board;
import com.gh.levelupboard.domain.user.Role;

public class BoardListDto {

    private Long id;
    private String name;
    private boolean current;
    private Role createPermission;

    public BoardListDto(Board board) {
        this(board, null);
    }

    public BoardListDto(Board board, Long currentBoardId) {
        id = board.getId();
        name = board.getName();
        current = id.equals(currentBoardId);
        createPermission = board.getCreatePermission();
    }

}
