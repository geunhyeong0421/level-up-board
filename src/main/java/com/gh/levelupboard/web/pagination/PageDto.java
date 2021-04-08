package com.gh.levelupboard.web.pagination;

public class PageDto {

    private int page;
    private boolean isCurrentPage;

    public PageDto(int page, boolean isCurrentPage) {
        this.page = page;
        this.isCurrentPage = isCurrentPage;
    }

}
