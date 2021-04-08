package com.gh.levelupboard.domain.post;

import com.gh.levelupboard.web.post.dto.Criteria;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import java.util.Arrays;
import java.util.HashSet;
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
        String type = cri.getTypeAfterSet();
        String keyword = cri.getKeyword();

        List<Post> content = queryFactory
                .selectFrom(post)
                .join(post.board, board).fetchJoin()
                .join(post.user, user).fetchJoin()
                .where(boardIdEq(boardId), keywordEq(type, keyword))
                .orderBy(post.groupId.desc(), post.groupOrder.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(post)
                .where(boardIdEq(boardId), keywordEq(type, keyword))
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression boardIdEq(Long boardId) {
        return boardId != null ? board.id.eq(boardId) : null;
    }

    private BooleanExpression keywordEq(String type, String keyword) {
        if (keyword == null) {
            return null;
        }

        String[] keywordSegments = keyword.split("\\s+");
        String likeKeyword = "%";
        for (String keywordSegment : keywordSegments) {
            likeKeyword += keywordSegment + "%";
        }

        BooleanExpression expression = null;
        if (type.contains("T")) {
            expression = post.title.like(likeKeyword);
        }
        if (type.contains("C")) {
            expression = expression != null ? expression.or(post.content.like(likeKeyword)) : post.content.like(likeKeyword);
        }
        if (type.contains("W")) {
            expression = expression != null ? expression.or(user.name.like(likeKeyword)) : user.name.like(likeKeyword);
        }
        return expression;
    }


}
