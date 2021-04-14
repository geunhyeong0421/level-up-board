package com.gh.levelupboard.web.pagination;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Pagination {

    COMMENT(20, 5),
    POST(10, 10);

    private final int size; // 페이징 크기(화면에 출력할 데이터 수)
    private final int navSize; // 페이지 목록의 크기(화면에 출력할 페이지 수)

}
