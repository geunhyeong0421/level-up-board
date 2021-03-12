package com.gh.levelupboard.web.pagination;

public class PaginationDto {
    private int page;
    private boolean currentPage;

    public PaginationDto(int page, boolean currentPage) {
        this.page = page;
        this.currentPage = currentPage;
    }
}
