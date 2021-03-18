package com.gh.levelupboard.service.board;

import com.gh.levelupboard.web.board.dto.BoardListDto;

import java.util.List;

public interface BoardService {

    List<BoardListDto> getList();

    List<BoardListDto> getList(Long id);

}
