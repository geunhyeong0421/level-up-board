package com.gh.levelupboard.domain.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gh.levelupboard.domain.BaseTimeEntity;
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
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    private Long groupId; // 조회 쿼리의 order by에 사용(페이징을 위해)

    private String content;
    private boolean isSecret;
    private boolean isDeleted;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();


    @Builder
    public Comment(Long id, Post post, User user, String content, boolean isSecret, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id; // 댓글 번호
        this.post = post; // 게시글 번호
        this.user = user; // 작성자
        this.content = content; // 내용
        this.isSecret = isSecret; // 비밀 여부
        super.createdDate = createdDate;
        super.modifiedDate = modifiedDate;
    }

//=========================== JPA 설정 ======================================
// GenerationType.IDENTITY 설정으로 영속성 컨텍스트 포함에 필요한 id 값을 insert 쿼리로 얻어온다.
// insert쿼리 -> persist -> 메소드(setGroupId()) 실행 -> dirty checking에 의한 update 쿼리(테스트에선 em.flush())
    @PostPersist
    public void setGroupId() { // 부모가 있으면 부모의 id, 없으면 생성된 자신의 id
        groupId = parent != null ? parent.id : id;
    }
//===========================================================================


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
