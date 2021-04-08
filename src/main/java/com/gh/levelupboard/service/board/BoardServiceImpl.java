package com.gh.levelupboard.service.board;

import com.gh.levelupboard.domain.board.Board;
import com.gh.levelupboard.domain.board.BoardRepository;
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

//    private final PostRepository postRepository;
//    private final CommentRepository commentRepository;

    @Transactional
    @PostConstruct
    public void init() {
        if(boardRepository.count() == 0) {
            boardRepository.save(new Board("자유게시판")); // 1L
            boardRepository.save(new Board("테스트게시판", Role.ADMIN)); // 2L
            boardRepository.save(new Board("공지사항", Role.ADMIN)); // 3L
        }
        boards = boardRepository.findAll(Sort.by("id"));

//        if (postRepository.count() == 0) {
//            String[] foods = {"중독족발", "금계찜닭", "새천년육회", "장어의꿈", "호두갈비"};
//            int[] counts = new int[foods.length];
//
//            for (int i = 1; i <= 1000; i++) {
//                int random = (int) (Math.random() * foods.length); // 0 ~ 4
//
//                Post post = Post.builder()
//                        .board(get(2L))
//                        .title("게시글 테스트 " + i + ", keyword: " + foods[random] + ++counts[random])
//                        .content("테스트용 " + i + "번째 게시글입니다.")
//                        .build();
//                postRepository.save(post);
//            }
//
//            Post post = postRepository.findById(1000L).get();
//            for (int i = 1; i <= 300; i++) {
//                Comment comment = Comment.builder()
//                        .post(post)
//                        .content("테스트용 " + i + "번째 댓글입니다.")
//                        .build();
//                commentRepository.save(comment);
//            }
//        }
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
