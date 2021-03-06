package com.gh.levelupboard.web.post;

import com.gh.levelupboard.config.auth.LoginUser;
import com.gh.levelupboard.config.auth.dto.SessionUser;
import com.gh.levelupboard.service.post.PostService;
import com.gh.levelupboard.web.post.dto.PostSaveRequestDto;
import com.gh.levelupboard.web.post.dto.PostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class PostApiController {

    private final PostService postService;

    // 등록
    @PostMapping("/api/v1/posts")
    public Long addPost(@LoginUser SessionUser user, @RequestBody PostSaveRequestDto requestDto) {
        requestDto.setUserId(user.getId());
        return postService.add(requestDto);
    }

    // 수정
    @PutMapping("/api/v1/posts/{id}")
    public Long modifyPost(@PathVariable Long id, @RequestBody PostUpdateRequestDto requestDto) {
        return postService.modify(id, requestDto);
    }

    // 삭제
    @DeleteMapping("/api/v1/posts/{id}")
    public Long removePost(@PathVariable Long id) {
        return postService.remove(id);
    }


}
