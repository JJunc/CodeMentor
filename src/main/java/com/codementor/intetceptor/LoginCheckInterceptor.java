package com.codementor.intetceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.URLEncoder;

public class LoginCheckInterceptor implements HandlerInterceptor {

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

        return true; // 로그인 되어 있으면 통과
    }
}
