package com.gh.levelupboard.domain.comment;

import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;
    @OneToMany(mappedBy = "parent") // 단순 조회용
    private List<Comment> children = new ArrayList<>();

    private Long groupId; // 조회 쿼리의 order by에 사용

    @Column(length = 500)
    private String content;

    private boolean isSecret; // 비밀 여부
    private boolean isDeleted; // 삭제 여부

    @Column(updatable = false)
    private LocalDateTime createdDate; // 작성일
    private LocalDateTime modifiedDate; // 최종 수정일


    @Transient
    private boolean isGroupIdUpdated;


    @Builder
    public Comment(Long id, Post post, User user, String content, boolean isSecret, LocalDateTime createdDate) {
        this.id = id;

        this.post = post;
        this.user = user;

        this.content = content;
        this.isSecret = isSecret;

        this.modifiedDate = this.createdDate = createdDate;
    }


//=========================== JPA 관련 설정 ======================================
/*
    GenerationType.IDENTITY 설정으로 영속성 컨텍스트 포함에 필요한 id 값을 insert 쿼리로 얻어온다. 실행 순서는
    @PrePersist -> insert 쿼리 -> @PostPersist -> Dirty Checking에 의한 update 쿼리(테스트에선 em.flush() 사용)
 */
    @PrePersist // insert 쿼리 직전
    public void prePersist() {
        modifiedDate = createdDate = LocalDateTime.now();
    }
    @PostPersist // insert 쿼리 직후
    public void postPersist() {
        if (groupId == null) {
            groupId = id; // insert 쿼리 이후 생성된 본인의 id로 그룹 설정
            isGroupIdUpdated = true;
        }
    }
    @PreUpdate // update 쿼리 직전
    public void preUpdate() { // 수정일 변경에만 관여
        if (isGroupIdUpdated) {
            isGroupIdUpdated = false; // 초기화
            return;
        }
        modifiedDate = LocalDateTime.now();
    }
//==============================================================================


    //== 연관 관계 메서드 ==//
    public void setParent(Comment parent) {
        this.parent = parent;
        this.groupId = parent.groupId; // 부모의 그룹으로 그룹 설정
        parent.children.add(this);
    }

    // 댓글 수정
    public void update(String content, boolean isSecret) {
        this.content = content;
        this.isSecret = isSecret;
    }

    // 삭제 상태로 변경(답글이 있는 경우)
    public void setDeleted() {
        this.isDeleted = true;
    }


}
