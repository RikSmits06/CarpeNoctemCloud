package org.carpenoctemcloud.controllers.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.carpenoctemcloud.account.Account;
import org.carpenoctemcloud.auth.AuthTokenService;
import org.carpenoctemcloud.auth.CurrentUserContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.RequestScope;

@RequestMapping("/auth")
@RequestScope
@Controller
public class LogoutController {

    private final AuthTokenService authTokenService;
    private final CurrentUserContext currentUserContext;

    public LogoutController(AuthTokenService authTokenService,
                            CurrentUserContext currentUserContext) {
        this.authTokenService = authTokenService;
        this.currentUserContext = currentUserContext;
    }

    @GetMapping({"/logout", "/logout."})
    public String logoutPage(HttpServletResponse response) {
        Optional<Account> accountOpt = currentUserContext.getUser();

        if (accountOpt.isEmpty()) {
            return "redirect:/auth/login";
        }

        Account account = accountOpt.get();
        authTokenService.deleteTokensOfAccount(account.id());
        Cookie cookie = new Cookie("auth-token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/auth/login";
    }
}
