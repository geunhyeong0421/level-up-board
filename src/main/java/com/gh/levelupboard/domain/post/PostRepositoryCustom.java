package com.gh.levelupboard.domain.post;

import com.gh.levelupboard.web.post.dto.Criteria;
import org.springframework.data.domain.Page;



public interface PostRepositoryCustom {

    Page<Post> findByBoardIdWithPagination(Long boardId, Criteria cri);

}
