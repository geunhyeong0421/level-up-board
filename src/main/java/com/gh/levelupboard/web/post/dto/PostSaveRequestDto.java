package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.domain.board.Board;
import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostSaveRequestDto { // 게시글 등록 요청 정보

    private Long boardId; // 게시판
    private Long userId; // 작성자(로그인 유저)
    private String title; // 제목
    private String content; // 내용


    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Post toEntity(Board board, User user) {
        return Post.builder()
                .board(board)
                .user(user)
                .title(this.title)
                .content(this.content)
                .build();
    }

}
