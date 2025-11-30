package org.carpenoctemcloud.controllers.auth;

import org.carpenoctemcloud.auth.CurrentUserContext;
import org.carpenoctemcloud.email_confirmation.EmailConfirmationTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestScope
@RequestMapping("/auth")
public class EmailConfirmationController {

    private final CurrentUserContext currentUserContext;
    private final EmailConfirmationTokenService emailConfirmationTokenService;

    public EmailConfirmationController(CurrentUserContext currentUserContext,
                                       EmailConfirmationTokenService emailConfirmationTokenService) {
        this.currentUserContext = currentUserContext;
        this.emailConfirmationTokenService = emailConfirmationTokenService;
    }

    @GetMapping({"/confirm", "/confirm/"})
    public String confirmEmailPage(
            @RequestParam(name = "error", required = false, defaultValue = "0") int error,
            Model model) {
        if (currentUserContext.userExists()) {
            return "redirect:/";
        }

        String errorMsg;
        switch (error) {
            case 0 -> errorMsg = "";
            case 1 -> errorMsg = "Could not confirm email.";
            default -> errorMsg = "Invalid error message code.";
        }

        model.addAttribute("error", errorMsg);

        return "emailConfirmPage";
    }

    @PostMapping({"/confirm", "/confirm/"})
    public String confirmEmailProcedure(@ModelAttribute(name = "token") String token) {
        if (currentUserContext.userExists()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              "User already has a confirmed email.");
        }

        try {
            emailConfirmationTokenService.activateEmailByToken(token);
        } catch (RuntimeException e) {
            return "redirect:/auth/confirm?error=1";
        }

        return "redirect:/auth/login";
    }
}
