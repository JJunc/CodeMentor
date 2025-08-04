package com.codementor.member.controller;

import com.codementor.comment.service.CommentService;
import com.codementor.exception.InvalidPasswordException;
import com.codementor.member.dto.*;
import com.codementor.member.enums.CheckPassword;
import com.codementor.member.enums.SessionConst;
import com.codementor.member.service.MemberService;
import com.codementor.post.enums.PostCategory;
import com.codementor.post.service.PostService;
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
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyPageController {

    private final MemberService memberService;
    private final CommentService commentService;

    @GetMapping
    public String myPage(HttpSession session,
                         Model model,
                         @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        LoginResponseDto loginMember = (LoginResponseDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        model.addAttribute("member", memberService.memberToMyPageDto(loginMember.getUsername()));
        return "/member/mypage";
    }

    @GetMapping("/posts/{category}")
    public String myPosts(HttpSession session,
                          @PathVariable PostCategory category,
                          Model model,
                            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        if (category == null) {
            model.addAttribute("category", category.FREE);
            return "redirect:/post/FREE";
        }

        LoginResponseDto loginMember = (LoginResponseDto) session.getAttribute(SessionConst.LOGIN_MEMBER);

        log.info("카테고리 = {}", category);

        model.addAttribute("category", category);
        model.addAttribute("posts", memberService.getMyPostList(loginMember.getUsername(), category, pageable));

        return "/member/mypage-posts";
    }

    @GetMapping("/comments")
    public String myComments(HttpSession session,
                          Model model,
                          @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        LoginResponseDto loginMember = (LoginResponseDto) session.getAttribute(SessionConst.LOGIN_MEMBER);

        model.addAttribute("comments", commentService.getMyComments(loginMember.getUsername(), pageable));

        return "/member/mypage-comments";
    }

    @PostMapping("/edit/nickname")
    @ResponseBody
    public ResponseEntity editNickname(HttpSession session
            , @Validated @RequestBody MemberEditNicknameDto dto
            , BindingResult bindingResult) {

        LoginResponseDto loginMember = (LoginResponseDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        EditResponseDto response = new EditResponseDto();

        log.info("닉네임 변경 = {}", dto.getNickname());

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(response);
        }

        boolean nicknameError = memberService.editNickname(loginMember.getUsername(), dto);

        if (!nicknameError) {
            response.setSuccess(false);
            return ResponseEntity.badRequest().body(response);
        }

        log.info("변경된 닉네임= {}", dto.getNickname());

        response.setSuccess(true);
        response.setData(dto.getNickname());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/edit/email")
    @ResponseBody
    public ResponseEntity editEmail(
            HttpSession session,
            @Validated @RequestBody MemberEmailUpdateDto dto,
            BindingResult bindingResult) {
        log.info("입력한 이메일 ={}", dto.getEmail());

        LoginResponseDto loginMember = (LoginResponseDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        EditResponseDto response = new EditResponseDto();

        if (bindingResult.hasErrors()) {
            log.info("유효성 검증 실패");
            return ResponseEntity.badRequest().body(response);
        }

        // 비밀번호 변경 요청 처리
        boolean emailError = memberService.editEmail(loginMember.getUsername(), dto);

        log.info("이메일 중복 발생 여부 = {}", emailError);

        // 오류 코드들을 순회하면서 리스트에 추가
        if (!emailError) { // SUCCESS가 아닌 경우에만 추가
            response.setSuccess(false);
            log.info("이메일 중복");
            return ResponseEntity.badRequest().body("이메일 중복");
        }


        response.setData(dto.getEmail());
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/edit/password")
    @ResponseBody
    public ResponseEntity editPassword(
            HttpSession session,
            @Valid @RequestBody MemberEditPasswordDto dto,
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
            Optional<FieldError> currentPassword = bindingResult.getFieldErrors().stream()
                    .filter(e -> e.getField().equals("currentPassword")) // 특정 필드만 필터링
                    .findFirst(); // 첫 번째 에러 찾기

            Optional<FieldError> newPassword = bindingResult.getFieldErrors().stream()
                    .filter(e -> e.getField().equals("password")) // 특정 필드만 필터링
                    .findFirst(); // 첫 번째 에러 찾기


            if (currentPassword.isEmpty() || newPassword.isEmpty()) {
                List<CheckPassword> passwordErrors = memberService.checkPassword(loginMember.getUsername(), dto);
                for (CheckPassword error : passwordErrors) {
                    if (error != CheckPassword.SUCCESS) {
                        log.info("비밀번호 에러코드 = {}", error.getErrorCode());
                        errorCodes.add(error.getErrorCode());
                        messages.add(error.getMessage());
                    }

                    if (!errorCodes.isEmpty()) {
                        response.setSuccess(false);
                        response.setErrorCodes(errorCodes);
                        response.setMessages(messages);
                        return ResponseEntity.badRequest().body(response);
                    }
                }
                return ResponseEntity.badRequest().body(response);
            }
        }

        // 비밀번호 변경 요청 처리
        List<CheckPassword> passwordErrors = memberService.editPassword(loginMember.getUsername(), dto);

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

    @PostMapping("/api/delete")
    @ResponseBody
    public ResponseEntity deleteMember(HttpSession session, @RequestBody MemberDeleteDto dto) {
        LoginResponseDto loginMember = (LoginResponseDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        log.info("비밀번호 입력: {}", dto.getPassword());
        try {
            memberService.deleteMember(loginMember.getUsername(), dto.getPassword());
            session.invalidate();
            return ResponseEntity.ok().build();
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}