package com.codementor.member.controller;

import com.codementor.member.dto.LoginRequestDto;
import com.codementor.member.dto.LoginResponseDto;
import com.codementor.member.dto.SignUpRequestDto;
import com.codementor.member.enums.MemberStatus;
import com.codementor.member.enums.SessionConst;
import com.codementor.member.service.MemberService;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/check-username")
    @ResponseBody
    public ResponseEntity<String> checkUsername(@RequestBody SignUpRequestDto dto) {
        boolean check = memberService.checkUsername(dto);

        if (check) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 아이디입니다.");
        }

        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }

    @PostMapping("/check-email")
    @ResponseBody
    public ResponseEntity<String> checkEmail(@RequestBody SignUpRequestDto dto) {
        boolean check = memberService.checkEmail(dto);

        if (check) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 이메일입니다.");
        }

        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }

    @PostMapping("/check-nickname")
    @ResponseBody
    public ResponseEntity<String> checkNickname(@RequestBody SignUpRequestDto dto) {
        boolean check = memberService.checkEmail(dto);

        if (check) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 이메일입니다.");
        }

        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }

    @PostMapping("/check-password")
    @ResponseBody
    public ResponseEntity<String> checkPassword(@RequestBody SignUpRequestDto dto) {

        boolean check = memberService.checkPassword(dto);

        if (!check) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("비밀번호가 일치하지 않습니다.");
        }

        return ResponseEntity.ok("비밀번호가 일치합니다.");
    }

    @PostMapping("/signUp")
    public String signUp(@Valid @ModelAttribute("signUpDto") SignUpRequestDto dto, BindingResult bindingResult, Model model,RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("signUpDto", dto);
            return "/member/signUp-form";
        }

        memberService.signUp(dto);
        redirectAttributes.addFlashAttribute("message", "회원가입 성공! 로그인 후 이용해 주세요.");
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginDto", new LoginRequestDto());
        return "/member/login-form";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginDto") LoginRequestDto dto,
                        BindingResult bindingResult, Model model, HttpServletRequest request) {

        LoginResponseDto loginMember = memberService.login(dto);

        if (bindingResult.hasErrors()) {
            model.addAttribute("loginDto", dto);
            return "/member/login-form";
        }

        if (loginMember == null) {
            model.addAttribute("loginDto", dto);
            model.addAttribute("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "/member/login-form";
        }

        if(loginMember.getStatus() == MemberStatus.SUSPENDED){
            model.addAttribute("loginDto", dto);
            model.addAttribute("suspended", "정지된 회원 입니다.");
            model.addAttribute("endDate", "정지 기간: " + loginMember.getStartDate() + " ~ " + loginMember.getEndDate() );
        }

        if(loginMember.getStatus() == MemberStatus.BANNED){
            model.addAttribute("loginDto", dto);
            model.addAttribute("suspended", "영구정지된 회원 입니다.");
            model.addAttribute("endDate", "정지 기간: " + loginMember.getStartDate() + " ~ " + loginMember.getEndDate() );
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
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

}
