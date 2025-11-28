package org.carpenoctemcloud.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@SuppressWarnings("NullableProblems")
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        String token = request.getHeader("auth-token");

        if (Objects.isNull(token)) {
            return true;
        }

        // TODO: implement setting the current account and calling the database.
        return true;
    }
}
