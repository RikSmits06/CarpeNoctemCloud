package org.carpenoctemcloud.controllers.auth;

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
    public String logoutPage() {
        Optional<Account> accountOpt = currentUserContext.getUser();

        if (accountOpt.isEmpty()) {
            return "redirect:/auth/login";
        }

        Account account = accountOpt.get();
        authTokenService.deleteTokensOfAccount(account.id());
        return "redirect:/auth/login";
    }
}
