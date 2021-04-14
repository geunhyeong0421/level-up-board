package com.gh.levelupboard.web.pagination;

public class PageDto {

    private int page; // 출력할 페이지
    private boolean isCurrentPage; // 현재 페이지 유무

    public PageDto(int page, boolean isCurrentPage) {
        this.page = page;
        this.isCurrentPage = isCurrentPage;
    }

}
