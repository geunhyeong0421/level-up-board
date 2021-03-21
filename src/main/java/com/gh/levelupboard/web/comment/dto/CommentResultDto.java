package com.gh.levelupboard.web.comment.dto;

import com.gh.levelupboard.web.pagination.Pagination;
import lombok.Getter;

@Getter
public class CommentResultDto {

    private Long targetId;
    private int pageIndex;

    public CommentResultDto(Long targetId, int targetRownum) {
        this.targetId = targetId;
        pageIndex = (int) Math.ceil(1.0 * targetRownum / Pagination.COMMENT.getSize()) - 1;
    }

}
