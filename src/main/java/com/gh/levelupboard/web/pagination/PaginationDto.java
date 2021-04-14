package com.gh.levelupboard.web.pagination;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PaginationDto {

    private long total; // 조회(검색) 결과 전체 데이터 수

    private Integer firstPage; // 전체 페이지 중 첫 페이지
    private Integer prevPage; // 이전 페이지 목록의 끝 페이지
    private List<PageDto> pages = new ArrayList<>(); // 화면에 출력할 현재 페이지 목록
    private Integer nextPage; // 다음 페이지 목록의 시작 페이지
    private Integer lastPage; // 전체 페이지 중 마지막 페이지


    public PaginationDto(Page pageResult, int navSize) { // 페이징 결과와 페이지 목록의 크기를 인자로 받음
        if (pageResult.hasContent()) { // 표시할 결과가 있으면 로직을 수행
            total = pageResult.getTotalElements(); // 카운트 쿼리의 결과

            int currentPage = pageResult.getNumber() + 1; // Spring Data의 page는 zero-based이므로 + 1을 해준다.
            int totalPages = pageResult.getTotalPages(); // 전체 페이지 수

            int currentNav = (int) Math.ceil(1.0 * currentPage / navSize); // 현재 페이지 목록의 순번
            int totalNavs = (int) Math.ceil(1.0 * totalPages / navSize); // 전체 페이지 목록의 수

            int startPage = (currentNav - 1) * navSize + 1; // 현재 페이지 목록의 시작 페이지
            int endPage = currentNav != totalNavs ? currentNav * navSize : totalPages; // 현재 페이지 목록의 끝 페이지

            // 이동할 페이지가 있다면 해당 값으로, 아니면 null
            firstPage = !pageResult.isFirst() ? 1 : null; // 첫 페이지
            prevPage = currentNav > 1 ? startPage - 1 : null; // 현재 페이지 목록의 시작 페이지 - 1
            nextPage = currentNav < totalNavs ? endPage + 1 : null; // 현재 페이지 목록의 끝 페이지 + 1
            lastPage = !pageResult.isLast() ? totalPages : null; // 마지막 페이지

            for (int page = startPage; page <= endPage; page++) { // Mustache 사용을 위한 값 + 현재 페이지 유무 설정
                pages.add(new PageDto(page, page == currentPage));
            }
        } // if-END
    }

}
