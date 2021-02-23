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

    private Long groupId; // 조회 쿼리의 order by에 사용(페이징을 위해)

    private String content;
    private boolean isSecret;
    private boolean isDeleted;

    @Column(updatable = false)
    private LocalDateTime createdDate; // 작성일
    private LocalDateTime modifiedDate; // 최종 수정일

    @OneToMany(mappedBy = "parent") // 단순 조회용
    private List<Comment> children = new ArrayList<>();

    @Transient
    private boolean isGroupIdUpdated;


    @Builder
    public Comment(Long id, Post post, User user, String content, boolean isSecret, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.post = post;
        this.user = user;
        this.content = content;
        this.isSecret = isSecret;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }


//=========================== JPA 관련 설정 ======================================
/*
    GenerationType.IDENTITY 설정 때문에 영속성 컨텍스트 포함에 필요한 id 값을 insert 쿼리로 얻어온다. 실행 순서는
    @PrePersist -> insert 쿼리 -> @PostPersist -> Dirty Checking에 의한 update 쿼리(테스트에선 em.flush() 사용)
 */
    @PrePersist // 직후에 insert 쿼리
    public void prePersist() {
        if (parent != null) { // 부모가 있으면 부모의 그룹으로 그룹 설정
            this.groupId = parent.groupId;
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ 부모의 그룹으로 그룹 설정 @@@@@@@@@@@@@@@@@@@@@@@@@");
        }
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        modifiedDate = now;
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ PrePersist 호출 완료 @@@@@@@@@@@@@@@@@@@@@@@@@");
    }
    @PostPersist // insert 쿼리 직후
    public void postPersist() {
        if (groupId != null) { return; } // 그룹 설정이 완료됐으면 return;
        groupId = id; // insert 쿼리 이후 생성된 본인의 id로 그룹 설정
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ 본인 id로 그룹 설정 @@@@@@@@@@@@@@@@@@@@@@@@@");
        isGroupIdUpdated = true; // groupId: null -> this.id
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ PostPersist 실행 완료 @@@@@@@@@@@@@@@@@@@@@@@@@");
    }
    @PreUpdate // update 쿼리 직전
    public void preUpdate() { // 수정일 변경에만 관여
        if (isGroupIdUpdated) { return; } // update 쿼리로 그룹을 설정할 땐 수정일을 변경하지 않음
        modifiedDate = LocalDateTime.now();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ PreUpdate 실행 완료 @@@@@@@@@@@@@@@@@@@@@@@@@");
    }
    @PostUpdate // update 쿼리 직후
    public void postUpdate() {
        if(!isGroupIdUpdated) { return; } // 초기화가 필요하지 않으면 return;
        isGroupIdUpdated = false; // 초기화
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ PostUpdate 실행 완료 @@@@@@@@@@@@@@@@@@@@@@@@@");
    }
//==============================================================================


    //== 연관 관계 메서드 ==//
    public void setParent(Comment parent) {
        this.parent = parent;
        parent.children.add(this);
    }

    // 댓글 수정
    public void update(String content, boolean isSecret) {
        this.content = content;
        this.isSecret = isSecret;
    }

    // 삭제 상태로 변경(대댓글이 있는 경우)
    public void delete() {
        this.isDeleted = true;
    }


}
