package com.gh.levelupboard.domain.post;

import com.gh.levelupboard.domain.board.Board;
import com.gh.levelupboard.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board; // 게시판(카테고리)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Post parent; // 답글일 경우 원글
    @OneToMany(mappedBy = "parent") // 단순 조회용(삭제 시에 사용)
    private List<Post> children = new ArrayList<>(); // 답글들

    private Long groupId; // 계층형 게시판 구현
    private int groupOrder; // 그룹내 순번
    private int depth; // 답글의 깊이

    private String title; // 제목
    @Lob
    private String content; // 내용

    private int hit; // 조회수
    private int commentsCount; // 댓글수

    private boolean isDeleted; // 삭제 여부

    @Column(updatable = false)
    private LocalDateTime createdDate; // 작성일
    private LocalDateTime modifiedDate; // 최종 수정일


    @Transient
    private int previousHit; // 조회수 변동 여부 확인용
    @Transient
    private int previousCommentsCount; // 댓글수 변동 여부 확인용
    @Transient
    private boolean isGroupIdUpdated;


    @Builder
    public Post(Long id, Board board, User user, String title, String content) {
        this.id = id;
        this.board = board;
        this.user = user;
        this.title = title;
        this.content = content;
    }

//==================== JPA 관련 설정 ============================
    @PostLoad // Entity가 영속성 컨텍스트에 조회된 직후
    public void postLoad() { // 로직 수행 전의 조회수와 댓글수를 기록
        previousHit = hit;
        previousCommentsCount = commentsCount;
    }
    @PrePersist
    public void prePersist() {
        modifiedDate = createdDate = LocalDateTime.now();
    }
    @PostPersist
    public void postPersist() {
        if (groupId == null) {
            groupId = id; // insert 쿼리 이후 생성된 본인의 id로 그룹 설정
            isGroupIdUpdated = true;
        }
    }
    @PreUpdate // update 쿼리 발생 직전
    public void preUpdate() { // 조회수 또는 댓글수 변동 시에는 수정일을 변경하지 않음
        if (previousHit != hit || previousCommentsCount != commentsCount || isGroupIdUpdated) {
            isGroupIdUpdated = false; // 초기화
            return;
        }
        modifiedDate = LocalDateTime.now(); // 제목 또는 내용 변경 시에는 수정일을 변경
    }
//=============================================================


    //== 연관 관계 메서드 ==//
    public void setParent(Post parent) {
        this.parent = parent;
        this.groupId = parent.groupId; // 부모글과 같은 그룹으로 설정
        this.depth = parent.depth + 1; // 부모글의 깊이 + 1
        parent.children.add(this);

        this.groupOrder = parent.groupOrder; // 부모글의 순번으로 초기 설정
        calculateGroupOrder(parent.children); // 재귀함수를 호출해서 최종 설정
    }

    // 재귀함수로 groupOrder를 계산(설정)
    private void calculateGroupOrder(List<Post> children) {
        for (Post child : children) {
            this.groupOrder++;
            calculateGroupOrder(child.children);
        }
    }

    // 게시글 수정
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 게시글 삭제 상태로 변경
    public void setDeleted() {
        this.isDeleted = true;
    }

    // 조회수 + 1
    public void increaseHit() {
        hit++;
    }

    // 댓글수 + 1
    public void increaseCommentsCount() {
        commentsCount++;
    }

    // 댓글수 - 1
    public void decreaseCommentsCount() {
        commentsCount--;
    }


}
