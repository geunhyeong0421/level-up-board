package com.gh.levelupboard.domain.post;

import com.gh.levelupboard.web.post.dto.Criteria;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import java.util.List;

import static com.gh.levelupboard.domain.board.QBoard.*;
import static com.gh.levelupboard.domain.post.QPost.*;
import static com.gh.levelupboard.domain.user.QUser.*;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> findByBoardIdWithPagination(Long boardId, Criteria cri) {
        Pageable pageable = cri.getPageable();

        List<Post> content = queryFactory
                .selectFrom(post)
                .join(post.board, board).fetchJoin()
                .join(post.user, user).fetchJoin()
                .where(boardIdEq(boardId))
                .orderBy(post.groupId.desc(), post.groupOrder.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(post)
                .where(boardIdEq(boardId))
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression boardIdEq(Long boardId) {
        return boardId != null ? board.id.eq(boardId) : null;
    }

}
