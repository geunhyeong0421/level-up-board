package com.gh.levelupboard.service.board;

import com.gh.levelupboard.domain.board.Board;
import com.gh.levelupboard.domain.board.BoardRepository;
import com.gh.levelupboard.web.board.dto.BoardListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private List<Board> boards;

    @Transactional
    @PostConstruct
    public void init() {
        if(boardRepository.count() == 0) {
            boardRepository.save(new Board("자유게시판"));
            boardRepository.save(new Board("테스트게시판"));
            boardRepository.save(new Board("공지사항"));
        }
        boards = boardRepository.findAll(Sort.by("id"));
    }

    @Override
    public List<BoardListDto> getList() {
        return boards.stream()
                .map(BoardListDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BoardListDto> getList(Long id) {
        return boards.stream()
                .map(board -> new BoardListDto(board, id))
                .collect(Collectors.toList());
    }

}
