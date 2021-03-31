package com.gh.levelupboard.web.post.dto;

import com.gh.levelupboard.web.pagination.Pagination;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Getter
@Setter
public class Criteria {

    private int page;
    private int size;

    public Criteria() {
        page = 1;
        size = Pagination.POST.getSize();
    }

    public Pageable getPageable() {
        return PageRequest.of(page - 1, size);
    }

}
