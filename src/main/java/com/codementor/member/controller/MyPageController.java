package com.codementor.member.controller;

import com.codementor.member.dto.EditResponseDto;
import com.codementor.member.dto.LoginResponseDto;
import com.codementor.member.dto.MemberEmailUpdateDto;
import com.codementor.member.dto.MemberUpdateDto;
import com.codementor.member.enums.CheckEmail;
import com.codementor.member.enums.CheckPassword;
import com.codementor.member.enums.SessionConst;
import com.codementor.member.service.MemberService;
import com.codementor.post.service.PostService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyPageController {

    private final PostService postService;
    private final MemberService memberService;

    @GetMapping
    public String myPage(HttpSession session,
                         Model model,
                         @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        LoginResponseDto loginMember = (LoginResponseDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        model.addAttribute("member", memberService.memberToMyPageDto(loginMember.getUsername()));
        return "/member/mypage";
    }

    @PostMapping("/check-email")
    @ResponseBody
    public ResponseEntity<String> checkEmail(@RequestBody MemberUpdateDto dto) {
        boolean check = memberService.checkEmail(dto);

        if (check) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 이메일입니다.");
        }

        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }

    @PostMapping("/check-nickname")
    @ResponseBody
    public ResponseEntity<String> checkNickname(@RequestBody MemberUpdateDto dto) {
        boolean check = memberService.checkEmail(dto);

        if (check) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 이메일입니다.");
        }

        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }

    @PostMapping("/edit/email")
    @ResponseBody
    public ResponseEntity editEmail(
            HttpSession session,
            @Valid @RequestBody MemberEmailUpdateDto dto,
            BindingResult bindingResult) {
        log.info("입력한 이메일 ={}", dto.getEmail());

        LoginResponseDto loginMember = (LoginResponseDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        EditResponseDto response = new EditResponseDto();

        if (bindingResult.hasErrors()) {
            log.info("유효성 검증 실패");
            return ResponseEntity.badRequest().body(response);
        }

        // 비밀번호 변경 요청 처리
        CheckEmail emailError = memberService.editEmail(loginMember.getUsername(), dto);

        log.info("이메일 에러코드 = {}", emailError.getErrorCode());

        // 오류 코드들을 순회하면서 리스트에 추가
        if (emailError != CheckEmail.SUCCESS) { // SUCCESS가 아닌 경우에만 추가
            response.setSuccess(false);
            response.setErrorCode(emailError.getErrorCode());
            response.setMessage(emailError.getMessage());
            log.info("이메일 에러코드 = {}", emailError.getErrorCode());
            return ResponseEntity.badRequest().body(response);
        }


        response.setData(dto.getEmail());
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/edit/password")
    @ResponseBody
    public ResponseEntity editPassword(
            HttpSession session,
            @Valid @RequestBody MemberUpdateDto dto,
            BindingResult bindingResult) {

        log.info("입력한 현재 비밀번호 = {}", dto.getCurrentPassword());
        log.info("입력한 변경 비밀번호 = {}", dto.getPassword());
        log.info("입력한 변경 비밀번호 확인 = {}", dto.getConfirmPassword());

        LoginResponseDto loginMember = (LoginResponseDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        EditResponseDto response = new EditResponseDto();

        // 에러 메시지를 담을 리스트
        List<String> errorCodes = new ArrayList<>();
        List<String> messages = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            log.info("유효성 검증 실패");

            Optional<FieldError> error = bindingResult.getFieldErrors().stream()
                    .filter(e -> e.getField().equals("currentPassword")) // 특정 필드만 필터링
                    .findFirst(); // 첫 번째 에러 찾기


            if (error.isEmpty()) {
                log.info("검증 실행");
                
                CheckPassword checkPassword = memberService.checkPassword(loginMember.getUsername(), dto);

                if (checkPassword != CheckPassword.SUCCESS) {
                    errorCodes.add(checkPassword.getErrorCode());
                    messages.add(checkPassword.getMessage());
                    response.setErrorCodes(errorCodes);
                    response.setMessages(messages);
                }
            }

            return ResponseEntity.badRequest().body(response);
        }

        // 비밀번호 변경 요청 처리
        List<CheckPassword> passwordErrors = memberService.editPassword(loginMember.getUsername(), dto);


        // 요청 값 유효성 검사

        // 오류 코드들을 순회하면서 리스트에 추가
        for (CheckPassword error : passwordErrors) {
            if (error != CheckPassword.SUCCESS) {
                log.info("비밀번호 에러코드 = {}", error.getErrorCode());
                errorCodes.add(error.getErrorCode());
                messages.add(error.getMessage());
            }
        }

        // 에러가 있는 경우 400 응답 반환
        if (!errorCodes.isEmpty()) {
            response.setSuccess(false);
            response.setErrorCodes(errorCodes);
            response.setMessages(messages);
            return ResponseEntity.badRequest().body(response);
        }

        // 성공 응답 반환
        response.setSuccess(true);
        response.setData(dto.getPassword());
        response.setMessage(CheckPassword.SUCCESS.getMessage());
        return ResponseEntity.ok(response);
    }

}