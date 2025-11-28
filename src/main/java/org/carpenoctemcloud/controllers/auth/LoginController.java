package org.carpenoctemcloud.controllers.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.carpenoctemcloud.auth.AuthConfiguration;
import org.carpenoctemcloud.auth.AuthTokenService;
import org.carpenoctemcloud.auth.CurrentUserContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/auth")
@RequestScope
public class LoginController {

    private final CurrentUserContext currentUserContext;
    private final AuthTokenService authTokenService;

    public LoginController(CurrentUserContext currentUserContext,
                           AuthTokenService authTokenService) {
        this.currentUserContext = currentUserContext;
        this.authTokenService = authTokenService;
    }

    @GetMapping({"/login", "/login/"})
    public String loginUserPage(@RequestParam(required = false, defaultValue = "0") int error,
                                Model model) {
        if (currentUserContext.userExists()) {
            return "redirect:/";
        }

        String errorMsg;
        switch (error) {
            case 0 -> errorMsg = null;
            case 1 -> errorMsg = "Could not login with the given credentials.";
            default -> errorMsg = "Invalid error code.";
        }

        model.addAttribute("error", errorMsg);
        return "loginPage";
    }

    @PostMapping({"/login", "/login/"})
    public String loginProcedure(@ModelAttribute(name = "email") String email,
                                 @ModelAttribute(name = "password") String password,
                                 HttpServletResponse response) {
        if (currentUserContext.userExists()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already logged in.");
        }

        Optional<String> tokenOpt = authTokenService.addAuthTokenByCredentials(email, password);

        if (tokenOpt.isEmpty()) {
            return "redirect:/auth/login?error=1";
        }

        Cookie cookie = new Cookie("auth-token", tokenOpt.get());
        cookie.setPath("/");
        cookie.setMaxAge(AuthConfiguration.MAX_AGE_IN_HOURS * 60 * 60);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
