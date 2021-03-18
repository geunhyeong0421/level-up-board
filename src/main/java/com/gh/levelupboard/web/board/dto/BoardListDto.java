package com.gh.levelupboard.web.board.dto;

import com.gh.levelupboard.domain.board.Board;

public class BoardListDto {

    private Long id;
    private String name;
    private boolean current;

    public BoardListDto(Board board) {
        this(board, null);
    }

    public BoardListDto(Board board, Long currentBoardId) {
        id = board.getId();
        name = board.getName();
        current = id.equals(currentBoardId);
    }

}
