package com.gh.levelupboard.web.comment.dto;

import com.gh.levelupboard.web.pagination.Pagination;
import lombok.Getter;

@Getter
public class CommentResultDto {

    private Long targetId;
    private int page;

    public CommentResultDto(Long targetId, int targetRownum) {
        this.targetId = targetId;
        page = (int) Math.ceil(1.0 * targetRownum / Pagination.COMMENT.getSize());
    }

}
