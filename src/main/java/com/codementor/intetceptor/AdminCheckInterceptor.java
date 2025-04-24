package com.codementor.intetceptor;

import com.codementor.member.dto.LoginResponseDto;
import com.codementor.member.entity.Member;
import com.codementor.member.enums.MemberRole;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

@lombok.extern.slf4j.Slf4j
@Slf4j
public class AdminCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        boolean isLoggedIn = session != null && session.getAttribute("loginMember") != null;

        if (!isLoggedIn) {
            response.sendRedirect("/member/login");
            return false;
        }

        LoginResponseDto loginMember = (LoginResponseDto) session.getAttribute("loginMember");

        log.info("loginMember : {}", loginMember.getUsername());
        log.info("loginMember role : {}", loginMember.getRole());

        if (!"admin".equals(loginMember.getUsername()) || !(loginMember.getRole() == MemberRole.ADMIN)) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자만 접근 가능합니다.");
            log.info("loginMember : {}", loginMember.getUsername());
            response.sendRedirect("/");
            return false;
        }

        return true;
    }
}

