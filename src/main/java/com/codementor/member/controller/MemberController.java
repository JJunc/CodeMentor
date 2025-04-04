package com.codementor.member.controller;

import com.codementor.member.dto.MemberSearchDto;
import com.codementor.member.dto.MemberListDto;
import com.codementor.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/search")
    public String searchMembers(@ModelAttribute MemberSearchDto memberSearchDto,
                                @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                Model model, RedirectAttributes redirectAttributes) {
        // 회원 검색 결과
        Page<MemberListDto> result = memberService.searchMembers(memberSearchDto, pageable);

        if (result.getTotalElements() == 0) {
            // 검색 결과가 없을 경우 알림 메시지 추가
            redirectAttributes.addFlashAttribute("noResults", "검색 조건에 맞는 회원이 없습니다.");
            return "redirect:/admin/members"; // 처음 페이지로 리다이렉트
        }

        model.addAttribute("members", result);
        return "admin/members";
    }
}
