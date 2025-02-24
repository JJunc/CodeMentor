package com.codementor.member.controller;

import com.codementor.member.dto.LoginRequestDto;
import com.codementor.member.dto.LoginResponseDto;
import com.codementor.member.dto.SignUpRequestDto;
import com.codementor.member.enums.SessionConst;
import com.codementor.member.service.MemberService;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        model.addAttribute("signUpDto", new SignUpRequestDto());
        return "/member/signUp-form";
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute("signUpDto") SignUpRequestDto signUpDto) {
        memberService.signUp(signUpDto);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginDto", new LoginRequestDto());
        return "/member/login-form";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginRequestDto dto, BindingResult
            bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "/member/login-form";
        }

        LoginResponseDto loginMember = memberService.login(dto);
        log.info("login? {}", loginMember);
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "/member/login-form";
        }
        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return "redirect:/";
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
