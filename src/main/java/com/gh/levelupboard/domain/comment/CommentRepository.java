package com.gh.levelupboard.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 페이징을 위한 준비
    @Query("select c from Comment c join c.post p join fetch c.user u where p.id = :postId order by c.groupId, c.id")
    List<Comment> findByPostId(@Param("postId") Long postId);

}
