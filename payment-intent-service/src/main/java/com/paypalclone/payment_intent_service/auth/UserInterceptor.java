package com.paypalclone.payment_intent_service.auth;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Component
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        System.out.println(">>> X-User-Id = " + request.getHeader("X-User-Id"));
        System.out.println(">>> X-Scopes = " + request.getHeader("X-Scopes"));

        String userId = request.getHeader("X-User-Id");
        String roles = request.getHeader("X-Scopes");

        if (userId != null) {
            UserContextHolder.setCurrentUserId(Long.valueOf(userId));
        }

        if (roles != null) {
            String[] rolesArray = roles.split(",");
            UserContextHolder.setCurrentUserRoles(Arrays.asList(rolesArray)); // store properly
        }


        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextHolder.clear();
    }
}
