package org.carpenoctemcloud.controllers.auth;

import org.carpenoctemcloud.account.AccountService;
import org.carpenoctemcloud.auth.CurrentUserContext;
import org.carpenoctemcloud.email.EmailService;
import org.carpenoctemcloud.email_confirmation.EmailConfirmationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/auth")
@RequestScope
public class SignUpController {

    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);
    private final AccountService accountService;
    private final CurrentUserContext currentUserContext;
    private final EmailConfirmationTokenService emailConfirmationTokenService;
    private final EmailService emailService;

    public SignUpController(AccountService accountService, CurrentUserContext currentUserContext,
                            EmailConfirmationTokenService emailConfirmationTokenService,
                            EmailService emailService) {
        this.accountService = accountService;
        this.currentUserContext = currentUserContext;
        this.emailConfirmationTokenService = emailConfirmationTokenService;
        this.emailService = emailService;
    }

    @GetMapping({"/signup", "/signup/"})
    public String createAccountPage(
            @RequestParam(name = "error", required = false, defaultValue = "0") int error,
            Model model) {
        if (currentUserContext.userExists()) {
            return "redirect:/";
        }

        String errorMsg;
        switch (error) {
            case 0 -> errorMsg = null;
            case 1 -> errorMsg =
                    "Failed to create account, make sure to use a @student.utwente.nl email address.";
            case 2 -> errorMsg = "Password is too short.";
            default -> errorMsg = "Invalid error code.";
        }
        model.addAttribute("error", errorMsg);
        return "signUpPage";
    }

    @PostMapping({"/signup", "/signup/"})
    public String createAccountProcedure(@ModelAttribute(name = "email") String email,
                                         @ModelAttribute(name = "password") String password,
                                         @ModelAttribute(name = "name") String name) {
        if (currentUserContext.userExists()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              "User already has an account.");
        }

        if (password.length() < 8) {
            return "redirect:/auth/signup?error=2";
        }

        try {
            accountService.createAccount(name, email, password, false);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "redirect:/auth/signup?error=1";
        }

        try {
            String token = emailConfirmationTokenService.generateConfirmationTokenByEmail(email);
            emailService.sendConfirmationEmail(email, token);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return "redirect:/auth/confirm";
    }
}
