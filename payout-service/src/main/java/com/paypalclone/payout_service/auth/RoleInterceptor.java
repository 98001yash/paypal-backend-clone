package com.paypalclone.payout_service.auth;




import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true; // allow non-controller requests
        }

        HandlerMethod method = (HandlerMethod) handler;
        RoleAllowed roleAllowed = method.getMethodAnnotation(RoleAllowed.class);

        // If method doesn't have @RoleAllowed, check at class level
        if (roleAllowed == null) {
            roleAllowed = method.getBeanType().getAnnotation(RoleAllowed.class);
        }

        // No role restriction, allow access
        if (roleAllowed == null) {
            return true;
        }

        String rolesHeader = String.valueOf(UserContextHolder.getCurrentUserRoles());
        if (rolesHeader == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            log.warn("Access denied: No roles found for user");
            return false;
        }

        Set<String> userRoles = new HashSet<>(UserContextHolder.getCurrentUserRoles());
        Set<String> allowedRoles = new HashSet<>(Arrays.asList(roleAllowed.value()));

        boolean hasAccess = userRoles.stream().anyMatch(allowedRoles::contains);

        if (!hasAccess) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            log.warn("Access denied: User roles {} do not match required roles {}", userRoles, allowedRoles);
        }

        return hasAccess;
    }
}