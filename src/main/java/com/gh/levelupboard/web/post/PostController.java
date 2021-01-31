package com.gh.levelupboard.web.post;

import com.gh.levelupboard.service.post.PostService;
import com.gh.levelupboard.web.post.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("posts", postService.getListDesc());
        return "index";
    }

    // 게시글 등록 화면
    @GetMapping("/posts/new")
    public String postsNew() {
        return "posts-new";
    }

    // 게시글 조회 화면
    @GetMapping("/posts/{id}")
    public String posts(@PathVariable Long id, Model model) {
        PostResponseDto dto = postService.get(id);
        model.addAttribute("post", dto);
        return "posts";
    }

    // 게시글 수정 화면
    @GetMapping("/posts/{id}/edit")
    public String postsEdit(@PathVariable Long id, Model model) {
        PostResponseDto dto = postService.get(id);
        model.addAttribute("post", dto);
        return "posts-edit";
    }
}
