package com.gh.levelupboard.web.comment.dto;

import com.gh.levelupboard.web.pagination.Pagination;
import lombok.Getter;

@Getter
public class CommentResultDto {

    private Long targetId;
    private int pageIndex;

    public CommentResultDto(Long commentId, int commentRownum) {
        targetId = commentId;
        pageIndex = commentRownum != 0 ? (int) Math.ceil(1.0 * commentRownum / Pagination.COMMENT.getSize()) - 1 : 0;
    }

}
