package com.gh.levelupboard.web.comment.dto;

import com.gh.levelupboard.domain.comment.Comment;
import com.gh.levelupboard.domain.post.Post;
import com.gh.levelupboard.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentSaveRequestDto { // 댓글 등록 요청 정보

    private Long postId; // 게시글 번호
    private Long userId; // 작성자(로그인 유저) id
    private Long parentId; // 부모 댓글 번호

    private String content; // 내용
    private boolean isSecret; // 비밀 댓글 여부


    public void setUserId(Long userId) { // 로그인 유저로 세팅
        this.userId = userId;
    }

    public Comment toEntity(Post post, User user, Comment parent) {
        Comment newComment = Comment.builder()
                .post(post)
                .user(user)
                .content(this.content)
                .isSecret(this.isSecret)
                .build();
        if (parent != null) {
            newComment.setParent(parent);
        }
        post.increaseCommentCount();
        return newComment;
    }

}
