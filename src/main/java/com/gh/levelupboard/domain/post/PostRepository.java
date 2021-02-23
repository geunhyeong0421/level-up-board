package com.gh.levelupboard.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p join fetch p.user where p.isDeleted = false order by p.id desc")
    List<Post> findAllDesc();

    @Query("select p from Post p join fetch p.user where p.id = :id")
    Optional<Post> findByIdFetch(@Param("id") Long id);

}
