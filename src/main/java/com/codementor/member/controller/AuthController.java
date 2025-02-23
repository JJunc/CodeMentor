package com.codementor.member.controller;

import com.codementor.member.dto.MemberLoginRequestDto;
import com.codementor.member.dto.MemberSignUpRequestDto;
import com.codementor.member.service.MemberService;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@lombok.extern.slf4j.Slf4j
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class AuthController {

    private final MemberService memberService;

    @GetMapping("/signUp")
    public String signIn(Model model) {
        model.addAttribute("signUpDto", new MemberSignUpRequestDto());
        return "/member/signUp-form";
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute("signUpDto") MemberSignUpRequestDto signUpDto) {
        memberService.signUp(signUpDto);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginDto", new MemberLoginRequestDto());
        return "/member/login-form";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginDto") MemberLoginRequestDto loginDto,
                       HttpServletRequest request, Model model) {

        boolean isMember = memberService.login(loginDto);

        log.info("로그인 요청 회원 유무 : {}", isMember);

        if(!isMember) {
            return "redirect:/member/login";
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("username", loginDto.getUsername());
            return "redirect:/";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

}
