package com.gh.levelupboard.web.pagination;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PaginationDto {

    private long total;

    private Integer firstPage;
    private Integer prevPage;
    private List<PageDto> pages = new ArrayList<>();
    private Integer nextPage;
    private Integer lastPage;


    public PaginationDto(Page pageResult, int navSize) {
        if (pageResult.hasContent()) {
            total = pageResult.getTotalElements();

            int currentPage = pageResult.getNumber() + 1;
            int totalPages = pageResult.getTotalPages();

            int currentNav = (int) Math.ceil(1.0 * currentPage / navSize);
            int totalNavs = (int) Math.ceil(1.0 * totalPages / navSize);

            int startPage = (currentNav - 1) * navSize + 1;
            int endPage = currentNav != totalNavs ? currentNav * navSize : totalPages;

            firstPage = !pageResult.isFirst() ? 1 : null;
            prevPage = currentNav > 1 ? startPage - 1 : null;
            nextPage = currentNav < totalNavs ? endPage + 1 : null;
            lastPage = !pageResult.isLast() ? totalPages : null;

            for (int i = startPage; i <= endPage; i++) {
                pages.add(new PageDto(i, i == currentPage));
            }
        } // if-END
    }


}
