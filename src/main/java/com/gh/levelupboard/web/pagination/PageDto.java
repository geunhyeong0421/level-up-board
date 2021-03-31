package com.gh.levelupboard.web.pagination;

public class PageDto {

    private int page;
    private boolean current;

    public PageDto(int page, boolean current) {
        this.page = page;
        this.current = current;
    }

}
