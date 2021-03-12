package com.gh.levelupboard.web.pagination;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Pagination {

    COMMENT(5, 5),
    POST(20, 10);

    private final int size; // 페이징 크기
    private final int navSize; // 화면에 출력되는 탐색(?) 페이지 수

}
