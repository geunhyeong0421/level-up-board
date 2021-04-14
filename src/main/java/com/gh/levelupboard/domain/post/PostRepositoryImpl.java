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

        String[] keywordSegments = keyword.split("\\s+"); // (연속)띄어쓰기를 기준으로 검색어를 나누어 구분한다
        String likeKeyword = "%";
        for (String keywordSegment : keywordSegments) { // 쿼리용 keyword를 조합한다
            likeKeyword += keywordSegment + "%";
        }

        BooleanExpression expression = null;
        if (type.contains("T")) { // type에 제목 포함
            expression = post.title.like(likeKeyword);
        }
        if (type.contains("C")) { // type에 내용 포함, 연결이 필요하면 or로 연결
            expression = expression != null ? expression.or(post.content.like(likeKeyword)) : post.content.like(likeKeyword);
        }
        if (type.contains("W")) { // type에 작성자 포함, 연결이 필요하면 or로 연결
            expression = expression != null ? expression.or(user.name.like(likeKeyword)) : user.name.like(likeKeyword);
        }
        return expression;
    }


}
