package com.codementor.admin.controller;

import com.codementor.admin.dto.MemberSuspensionDto;
import com.codementor.admin.service.AdminService;
import com.codementor.comment.dto.CommentRequestDto;
import com.codementor.comment.dto.CommentResponseDto;
import com.codementor.comment.dto.CommentSearchDto;
import com.codementor.comment.service.CommentService;
import com.codementor.member.dto.LoginResponseDto;
import com.codementor.member.dto.MemberRoleDto;
import com.codementor.member.enums.SessionConst;
import com.codementor.member.service.MemberService;
import com.codementor.post.dto.PostDeleteDto;
import com.codementor.post.dto.PostListDto;
import com.codementor.post.dto.PostSearchDto;
import com.codementor.post.dto.PostUpdateDto;
import com.codementor.post.enums.PostCategory;
import com.codementor.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;
    private final AdminService adminService;

    @GetMapping
    public String admin() {
        return "/admin/members";
    }

    @GetMapping("/members")
    public String members(HttpSession session,
                          Model model,
                          @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        LoginResponseDto loginMember = (LoginResponseDto) session.getAttribute(SessionConst.LOGIN_MEMBER);

        model.addAttribute("members", memberService.getAllMembers(pageable));
        log.info("members: {}", memberService.getAllMembers(pageable).stream().count());

        return "/admin/members";
    }


    @PostMapping("/member/role")
    public String updateMemberRole(HttpSession session,
                                   @ModelAttribute MemberRoleDto dto,
                                   @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        memberService.updateMemberRole(dto);

        return "redirect:/admin/members";
    }

    @PostMapping("/members/suspension")
    public String suspenseMember(HttpSession session,
                                 @ModelAttribute MemberSuspensionDto dto, BindingResult bindingResult, Model model) {
        adminService.suspenseMember(dto);

        return "redirect:/admin/members";
    }


    @GetMapping("/post/{category}")
    public String posts(@PathVariable PostCategory category
            , @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
            , Model model) {

        if (category == null) {
            return "redirect:/post/FREE";
        }

        model.addAttribute("category", category);
        model.addAttribute("posts", postService.getPostsByCategory(category, pageable));
        return "/admin/posts";
    }

    @GetMapping("/post/search")
    public String search(@ModelAttribute PostSearchDto searchDto
            , @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
            , Model model, RedirectAttributes redirectAttributes) {
        // 회원 검색 결과
        Page<PostListDto> result = postService.searchPosts(searchDto, pageable);

        log.info("검색 카테고리 = {}", searchDto.getCategory());

        if (result.isEmpty()) {
            redirectAttributes.addFlashAttribute("noResults", true); // 검색 결과 없음 플래그 추가
            return  "redirect:/admin/post/" + searchDto.getCategory(); // 다시 목록 페이지로 이동
        }

        model.addAttribute("category", searchDto.getCategory());
        model.addAttribute("posts", result);
        return "/admin/posts";
    }

    @PostMapping("/post/delete")
    public String deletePost(@ModelAttribute PostDeleteDto dto, RedirectAttributes redirectAttributes) {
        postService.deletePost(dto);

        log.info("삭제 될 게시글 번호 = {}", dto.getId());


        redirectAttributes.addAttribute("category", dto.getCategory());

        return "redirect:/admin/post/{category}";
    }

    @GetMapping("/comments")
    public String comments(@ModelAttribute CommentRequestDto dto,
                           Model model,
                           @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CommentResponseDto> commentDtoList = commentService.getComments(dto,pageable);

        model.addAttribute("comments", commentDtoList);

        return "/admin/comments";
    }

    @GetMapping("/comment/search")
    public String searchComment(@ModelAttribute CommentSearchDto dto, Model model,
                                @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){

        Page<CommentResponseDto> result = commentService.searchComments(dto, pageable);

        if (result.isEmpty()) {
            model.addAttribute("noResults", true); // 검색 결과 없음 플래그 추가
            return  "redirect:/admin/comments"; // 다시 목록 페이지로 이동
        }

        model.addAttribute("comments", result);

        return "/admin/comments";
    }

    @PostMapping("/comment/delete")
    public String deleteComment(@ModelAttribute CommentRequestDto dto, RedirectAttributes redirectAttributes) {
        commentService.delete(dto);

        return "redirect:/admin/comments";
    }

}
