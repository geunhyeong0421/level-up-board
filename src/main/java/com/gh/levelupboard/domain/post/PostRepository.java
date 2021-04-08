package com.gh.levelupboard.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Query("select p from Post p join fetch p.board join fetch p.user where p.id = :id")
    Optional<Post> findByIdForEdit(@Param("id") Long id);

    @Query("select p from Post p join fetch p.board where p.id = :id")
    Optional<Post> findByIdForReply(@Param("id") Long id);

    @Modifying
    @Query("update Post p set p.groupOrder = p.groupOrder + 1 where p.groupId = :groupId and p.groupOrder >= :groupOrder")
    int bulkGroupOrderPlus(@Param("groupId") Long groupId, @Param("groupOrder") int groupOrder);

    @Modifying
    @Query("update Post p set p.groupOrder = p.groupOrder - 1 where p.groupId = :groupId and p.groupOrder > :groupOrder")
    int bulkGroupOrderMinus(@Param("groupId") Long groupId, @Param("groupOrder") int groupOrder);

}
