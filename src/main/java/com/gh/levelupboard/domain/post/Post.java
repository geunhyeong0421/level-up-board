package com.gh.levelupboard.domain.post;

import com.gh.levelupboard.domain.BaseTimeEntity;
import com.gh.levelupboard.domain.board.Board;
import com.gh.levelupboard.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;


    // 자유 게시판(default)에 등록
    public Post(String title, String content, User user) {
        this(title, content, user, null);
    }

    public Post(String title, String content, User user, Board board) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.board = board;
    }

    // 게시글 수정
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
