package com.gh.levelupboard.web.post;

import com.gh.levelupboard.config.auth.LoginUser;
import com.gh.levelupboard.config.auth.dto.SessionUser;
import com.gh.levelupboard.domain.user.Role;
import com.gh.levelupboard.service.board.BoardService;
import com.gh.levelupboard.service.post.PostService;
import com.gh.levelupboard.web.pagination.Pagination;
import com.gh.levelupboard.web.pagination.PaginationDto;
import com.gh.levelupboard.web.post.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;


@RequiredArgsConstructor
@Controller
public class PostController {

    private final BoardService boardService;
    private final PostService postService;

    // 로그인 화면
    @GetMapping("/login")
    public void login() {}

    // 전체글 조회 화면(현재 홈 화면 겸용)
    @GetMapping(value = {"/posts", "/"})
    public String readAllPosts(Model model, @LoginUser SessionUser user, @ModelAttribute("cri") Criteria cri) {
        if (user != null) { model.addAttribute("loginUser", user); }
        model.addAttribute("boards", boardService.getList());
        model.addAttribute("allPosts", true);

        Page<PostListResponseDto> pageResult = postService.getList(cri);
        model.addAttribute("posts", pageResult.getContent());
        model.addAttribute("pageMaker", new PaginationDto(pageResult, Pagination.POST.getNavSize()));
        return "posts";
    }
    // 게시판(게시글 목록) 조회 화면
    @GetMapping("/boards/{boardId}/posts")
    public String readPosts(Model model, @LoginUser SessionUser user, @ModelAttribute("cri") Criteria cri,
                            @PathVariable Long boardId) {
        if (user != null) { model.addAttribute("loginUser", user); }
        model.addAttribute("boards", boardService.getList(boardId));
        model.addAttribute("boardName", boardService.get(boardId).getName());
        model.addAttribute("adminOnly", boardService.get(boardId).getCreatePermission().equals(Role.ADMIN));

        Page<PostListResponseDto> pageResult = postService.getList(boardId, cri);
        model.addAttribute("posts", pageResult.getContent());
        model.addAttribute("pageMaker", new PaginationDto(pageResult, Pagination.POST.getNavSize()));
        return "posts";
    }

//=====================================================================================

    // 게시글 등록 화면
    @GetMapping("/posts/new")
    public String createPost(Model model, @LoginUser SessionUser user, @ModelAttribute("cri") Criteria cri) {
        model.addAttribute("loginUser", user);
        model.addAttribute("boards", boardService.getList());
        model.addAttribute("allPosts", true);
        return "posts/create";
    }
    @GetMapping("/boards/{boardId}/posts/new")
    public String createPost(Model model, @LoginUser SessionUser user, @ModelAttribute("cri") Criteria cri,
                                    @PathVariable Long boardId) {
        if (boardService.get(boardId).getCreatePermission().equals(Role.ADMIN) && !user.isAdmin()) {
            return "forbidden";
        }
        model.addAttribute("loginUser", user);
        model.addAttribute("boards", boardService.getList(boardId));
        return "posts/create";
    }

    // 게시글 조회 화면
    @GetMapping("/posts/{id}")
    public String readPost(Model model, @LoginUser SessionUser user, @ModelAttribute("cri") Criteria cri,
                        @PathVariable Long id) {
        model.addAttribute("loginUser", user);
        model.addAttribute("boards", boardService.getList());
        model.addAttribute("allPosts", true);

        model.addAttribute("post", postService.get(id, user));
        return "posts/read";
    }
    @GetMapping("/boards/{boardId}/posts/{id}")
    public String readPost(Model model, @LoginUser SessionUser user, @ModelAttribute("cri") Criteria cri,
                           @PathVariable Long boardId, @PathVariable Long id) {
        model.addAttribute("loginUser", user);
        model.addAttribute("boards", boardService.getList(boardId));

        model.addAttribute("post", postService.get(id, user));
        return "posts/read";
    }

    // 게시글 수정 화면
    @GetMapping("/posts/{id}/edit")
    public String editPost(Model model, @LoginUser SessionUser user, @ModelAttribute("cri") Criteria cri,
                            @PathVariable Long id) {
        EditPostResponseDto dto = postService.getForEdit(id);
        if (!dto.getWriterId().equals(user.getId())) {
            return "forbidden";
        }
        model.addAttribute("post", dto);

        model.addAttribute("loginUser", user);
        model.addAttribute("boards", boardService.getList());
        model.addAttribute("allPosts", true);
        return "posts/edit";
    }
    @GetMapping("/boards/{boardId}/posts/{id}/edit")
    public String editPost(Model model, @LoginUser SessionUser user, @ModelAttribute("cri") Criteria cri,
                           @PathVariable Long boardId, @PathVariable Long id) {
        EditPostResponseDto dto = postService.getForEdit(id);
        if (!dto.getWriterId().equals(user.getId())) {
            return "forbidden";
        }
        model.addAttribute("post", dto);

        model.addAttribute("loginUser", user);
        model.addAttribute("boards", boardService.getList(boardId));
        return "posts/edit";
    }

    // 게시글 답글 화면
    @GetMapping("/posts/{id}/reply")
    public String replyPost(Model model, @LoginUser SessionUser user, @ModelAttribute("cri") Criteria cri,
                            @PathVariable Long id) {
        ReplyPostResponseDto dto = postService.getForReply(id);
        if (dto.isAdminOnly() && !user.isAdmin()) {
            return "forbidden";
        }
        model.addAttribute("post", dto);

        model.addAttribute("loginUser", user);
        model.addAttribute("boards", boardService.getList());
        model.addAttribute("allPosts", true);
        return "posts/reply";
    }
    @GetMapping("/boards/{boardId}/posts/{id}/reply")
    public String replyPost(Model model, @LoginUser SessionUser user, @ModelAttribute("cri") Criteria cri,
                            @PathVariable Long boardId, @PathVariable Long id) {
        ReplyPostResponseDto dto = postService.getForReply(id);
        if (dto.isAdminOnly() && !user.isAdmin()) {
            return "forbidden";
        }
        model.addAttribute("post", dto);

        model.addAttribute("loginUser", user);
        model.addAttribute("boards", boardService.getList(boardId));
        return "posts/reply";
    }

}
