package com.gh.levelupboard.web.post;

import com.gh.levelupboard.config.auth.LoginUser;
import com.gh.levelupboard.config.auth.dto.SessionUser;
import com.gh.levelupboard.service.board.BoardService;
import com.gh.levelupboard.service.post.PostService;
import com.gh.levelupboard.web.post.dto.EditPostResponseDto;
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
    private final BoardService boardService;


    // 로그인 화면
    @GetMapping("/login")
    public void login() {}

    // 전체글 조회 화면(현재 홈 화면 겸용)
    @GetMapping(value = {"/posts", "/"})
    public String readAllPosts(Model model, @LoginUser SessionUser user) {
        if (user != null) { model.addAttribute("loginUser", user); }
        model.addAttribute("posts", postService.getListDesc());
        model.addAttribute("allPosts", true);
        model.addAttribute("boards", boardService.getList());
        return "posts";
    }
    // 게시판(게시글 목록) 조회 화면
    @GetMapping("/boards/{boardId}/posts")
    public String readPosts(Model model, @LoginUser SessionUser user,
                            @PathVariable Long boardId) {
        if (user != null) { model.addAttribute("loginUser", user); }
        model.addAttribute("posts", postService.getListDesc());
        model.addAttribute("boards", boardService.getList(boardId));
        return "posts";
    }

//=====================================================================================

    // 게시글 등록 화면
    @GetMapping("/posts/new")
    public String createPost(Model model, @LoginUser SessionUser user) {
        model.addAttribute("loginUser", user);
        model.addAttribute("allPosts", true);
        model.addAttribute("boards", boardService.getList());
        return "posts/create";
    }
    @GetMapping("/boards/{boardId}/posts/new")
    public String createPost(Model model, @LoginUser SessionUser user,
                                    @PathVariable Long boardId) {
        model.addAttribute("loginUser", user);
        model.addAttribute("boards", boardService.getList(boardId));
        return "posts/create";
    }

    // 게시글 조회 화면
    @GetMapping("/posts/{id}")
    public String readPost(Model model, @LoginUser SessionUser user,
                        @PathVariable Long id) {
        model.addAttribute("loginUser", user);
        model.addAttribute("allPosts", true);
        model.addAttribute("boards", boardService.getList());

        PostResponseDto dto = postService.get(id, user);
        model.addAttribute("post", dto);
        return "posts/read";
    }
    @GetMapping("/boards/{boardId}/posts/{id}")
    public String readPost(Model model, @LoginUser SessionUser user,
                           @PathVariable Long boardId, @PathVariable Long id) {
        model.addAttribute("loginUser", user);
        model.addAttribute("boards", boardService.getList(boardId));

        PostResponseDto dto = postService.get(id, user);
        model.addAttribute("post", dto);
        return "posts/read";
    }

    // 게시글 수정 화면
    @GetMapping("/posts/{id}/edit")
    public String editPost(Model model, @LoginUser SessionUser user,
                            @PathVariable Long id) {
        model.addAttribute("loginUser", user);
        model.addAttribute("allPosts", true);
        model.addAttribute("boards", boardService.getList());

        EditPostResponseDto dto = postService.getForEdit(id);
        model.addAttribute("post", dto);
        return "posts/edit";
    }
    @GetMapping("/boards/{boardId}/posts/{id}/edit")
    public String editPost(Model model, @LoginUser SessionUser user,
                           @PathVariable Long boardId, @PathVariable Long id) {
        model.addAttribute("loginUser", user);
        model.addAttribute("boards", boardService.getList(boardId));

        EditPostResponseDto dto = postService.getForEdit(id);
        model.addAttribute("post", dto);
        return "posts/edit";
    }

}
