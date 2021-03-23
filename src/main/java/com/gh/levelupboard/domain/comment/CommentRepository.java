package com.gh.levelupboard.domain.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 페이징을 위한 준비
    @Query("select c from Comment c join fetch c.post p join fetch c.user u where p.id = :postId order by c.groupId, c.id")
    List<Comment> findByPostId(@Param("postId") Long postId);

    // 페이징 적용
    @Query(value = "select c from Comment c join fetch c.post p join fetch c.user u where p.id = :postId order by c.groupId, c.id"
            , countQuery = "select count(c) from Comment c join c.post p where p.id = :postId")
    Page<Comment> findByPostIdWithPagination(@Param("postId") Long postId, Pageable pageable);

    // 카운트 쿼리
    @Query("select count(c) from Comment c join c.post p where p.id = :postId")
    long countByPostId(@Param("postId") Long postId);

    // 댓글 등록, 수정, 삭제 이후 페이지 추적용
    @Query("select c.id from Comment c join c.post p where p.id = :postId order by c.groupId, c.id")
    List<Long> findIdByPostId(@Param("postId") Long postId);

    // 게시글 삭제시 on delete set null 적용
    @Modifying
    @Query(value = "update comment set post_id = null where post_id = :postId", nativeQuery = true)
    int bulkSetPostNull(@Param("postId") Long postId);

}
