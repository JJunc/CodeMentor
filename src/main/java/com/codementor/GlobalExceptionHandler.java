package com.codementor;

import com.codementor.exception.*;
import com.codementor.member.dto.LoginRequestDto;
import com.codementor.member.dto.LoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MemberNotFoundException.class, PostNotFoundException.class, CommentNotFoundException.class, IllegalStateException.class})
    public String handleMemberNotFoundException(Exception e) {
        return "/error/404";
    }

    @ExceptionHandler(LoginFailedException.class)
    public String handleLoginFailed(LoginFailedException e, HttpServletRequest request, Model model) {

        LoginRequestDto dto = new LoginRequestDto();
        dto.setUsername(request.getParameter("username"));
        model.addAttribute("loginFail", e.getMessage());
        model.addAttribute("loginDto", dto);
        return "/member/login-form";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e, HttpServletRequest request, Model model) {
        return "/error/500";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(Exception e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "/error/403";
    }

    @ExceptionHandler(ImageSaveException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleImageSaveException(ImageSaveException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("이미지 업로드에 실패했습니다."));
    }
}
