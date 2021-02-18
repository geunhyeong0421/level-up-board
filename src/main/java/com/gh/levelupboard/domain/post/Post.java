package com.gh.levelupboard.domain.post;

import com.gh.levelupboard.domain.board.Board;
import com.gh.levelupboard.domain.user.User;
import lombok.*;

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
    private Board board; // 게시판(카테고리)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 작성자

    private String title; // 제목
    private String content; // 내용

    private int hit; // 조회수
    private int commentCount; // 댓글수

    @Column(updatable = false)
    private LocalDateTime createdDate; // 작성일
    private LocalDateTime modifiedDate; // 최종 수정일

    @Transient
    private int previousHit; // 조회수 변동 여부 확인용
    @Transient
    private int previousCommentCount; // 댓글수 변동 여부 확인용


    @Builder
    public Post(Long id, Board board, User user, String title, String content) {
        this.id = id;
        this.board = board;
        this.user = user;
        this.title = title;
        this.content = content;
    }

//==================== JPA 관련 설정 ============================
    @PostLoad
    public void setPreviousHit() {
        previousHit = hit;
        previousCommentCount = commentCount;
    }
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        modifiedDate = now;
    }
    @PreUpdate
    public void preUpdate() { // 조회수 또는 댓글수 변동시에는 수정일을 변경하지 않음
        if (previousHit != hit || previousCommentCount != commentCount) { return; }
        modifiedDate = LocalDateTime.now();
    }
//=============================================================


    // 게시글 수정
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 작성자와 이용자의 일치 여부에 따른 조회수 증가
    public boolean isMyPost(Long loginUserId) {
        boolean isMyPost = this.user.getId().equals(loginUserId);
        if (!isMyPost) { // 본인 글이 아니면
            this.hit++; // 조회수 +1
        }
        return isMyPost; // 일치 여부를 반환
    }

    // 댓글수 +1
    public void increaseCommentCount() {
        commentCount++;
    }
    // 댓글수 -1
    public void decreaseCommentCount() {
        commentCount--;
    }


}
