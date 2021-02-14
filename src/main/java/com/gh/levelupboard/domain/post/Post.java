package com.gh.levelupboard.domain.post;

import com.gh.levelupboard.domain.board.Board;
import com.gh.levelupboard.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String content;
    private int hit;

    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Transient
    private int previousHit; // 조회수 변동 여부 확인용


//==================== JPA 관련 설정 ============================
    @PostLoad
    public void setPreviousHit() {
        previousHit = hit;
    }
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        modifiedDate = now;
    }
    @PreUpdate
    public void preUpdate() { // 조회수 변동시에는 수정일을 변경하지 않음
        if (previousHit != hit) { return; }
        modifiedDate = LocalDateTime.now();
    }
//=============================================================


    // 자유게시판(default)에 등록
    public Post(User user, String title, String content) {
        this(null, user, title, content);
    }

    public Post(Board board, User user, String title, String content) {
        this.board = board;
        this.user = user;
        this.title = title;
        this.content = content;
    }

    // 게시글 수정
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 조회수 +1
    public void increaseHit() {
        this.hit++;
    }


}
