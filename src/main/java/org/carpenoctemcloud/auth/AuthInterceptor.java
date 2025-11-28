package org.carpenoctemcloud.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import org.carpenoctemcloud.account.Account;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.HandlerInterceptor;

@SuppressWarnings("NullableProblems")
@RequestScope
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthTokenService authTokenService;
    private final CurrentUserContext currentUserContext;

    public AuthInterceptor(AuthTokenService authTokenService,
                           CurrentUserContext currentUserContext) {
        this.authTokenService = authTokenService;
        this.currentUserContext = currentUserContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        String token;
        try {
            token = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("auth-token")).toList().getFirst()
                    .getValue();
        } catch (NoSuchElementException e) {
            return true;
        }

        if (Objects.isNull(token)) {
            return true;
        }

        Optional<Account> accountOpt = authTokenService.accountFromToken(token);

        if (accountOpt.isEmpty()) {
            return true;
        }

        currentUserContext.setUser(accountOpt.get());

        return true;
    }
}
