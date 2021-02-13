package com.gh.levelupboard.web.post;

import com.gh.levelupboard.config.auth.LoginUser;
import com.gh.levelupboard.config.auth.dto.SessionUser;
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


    @GetMapping("/login")
    public void login() {}

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        if (user != null) { model.addAttribute("user", user); }
        model.addAttribute("posts", postService.getListDesc());
        return "index";
    }

    // 게시글 등록 화면
    @GetMapping("/posts/new")
    public String postsNew(Model model, @LoginUser SessionUser user) {
        model.addAttribute("user", user);
        return "posts/create";
    }

    // 게시글 수정 화면
    @GetMapping("/posts/{id}/edit")
    public String postsEdit(Model model, @LoginUser SessionUser user,
                            @PathVariable Long id) {
        model.addAttribute("user", user);

        PostResponseDto dto = postService.get(id);
        model.addAttribute("post", dto);
        return "posts/edit";
    }

    // 게시글 조회 화면
    @GetMapping("/posts/{id}")
    public String posts(Model model, @LoginUser SessionUser user,
                        @PathVariable Long id) {
        model.addAttribute("user", user);

        PostResponseDto dto = postService.get(id);
        model.addAttribute("post", dto);
        model.addAttribute("isMyPost", dto.getUserId().equals(user.getId()));
        return "posts/read";
    }

}
