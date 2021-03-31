package com.gh.levelupboard.service.board;

import com.gh.levelupboard.domain.board.Board;
import com.gh.levelupboard.domain.board.BoardRepository;
import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.domain.post.PostRepository;
import com.gh.levelupboard.domain.user.Role;
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

    private final PostRepository postRepository;

    @Transactional
    @PostConstruct
    public void init() {
        if(boardRepository.count() == 0) {
            boardRepository.save(new Board("자유게시판"));
            boardRepository.save(new Board("테스트게시판", Role.ADMIN));
            boardRepository.save(new Board("공지사항", Role.ADMIN));
        }
        boards = boardRepository.findAll(Sort.by("id"));

        for (int i = 1; i <= 100; i++) {
            Post post = Post.builder()
                    .board(boards.get(1))
                    .title("페이징 테스트 " + i)
                    .content("테스트용 " + i + "번째 글입니다.")
                    .build();
            postRepository.save(post);
        }
    }

    @Override
    public List<BoardListDto> getList() {
        return getList(null);
    }

    @Override
    public List<BoardListDto> getList(Long id) {
        return boards.stream()
                .map(board -> new BoardListDto(board, id))
                .collect(Collectors.toList());
    }

    @Override
    public Board get(Long id) {
        return boards.get(id.intValue() - 1);
    }

}
