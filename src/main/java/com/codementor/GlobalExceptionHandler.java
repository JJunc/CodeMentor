package com.codementor;

import com.codementor.exception.ErrorResponse;
import com.codementor.exception.ImageSaveException;
import com.codementor.member.exceptions.MemberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleMemberNotFoundException(MemberNotFoundException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
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
