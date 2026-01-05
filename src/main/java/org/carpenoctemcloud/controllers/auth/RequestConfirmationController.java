package org.carpenoctemcloud.controllers.auth;

import java.util.Optional;
import org.carpenoctemcloud.account.Account;
import org.carpenoctemcloud.account.AccountService;
import org.carpenoctemcloud.auth.CurrentUserContext;
import org.carpenoctemcloud.email.EmailService;
import org.carpenoctemcloud.email_confirmation.EmailConfirmationTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller which allows to request another email confirmation.
 */
@RequestScope
@Controller
@RequestMapping("/auth")
public class RequestConfirmationController {

    private final CurrentUserContext currentUserContext;
    private final AccountService accountService;
    private final EmailConfirmationTokenService emailConfirmationTokenService;
    private final EmailService emailService;

    /**
     * Generates a new controller.
     *
     * @param currentUserContext            The current user needed to check that only non-logged in users are connecting.
     * @param accountService                The service used to get account information.
     * @param emailConfirmationTokenService The service used to get new email tokens.
     * @param emailService                  The service used to send emails containing the token.
     */
    public RequestConfirmationController(CurrentUserContext currentUserContext,
                                         AccountService accountService,
                                         EmailConfirmationTokenService emailConfirmationTokenService,
                                         EmailService emailService) {
        this.currentUserContext = currentUserContext;
        this.accountService = accountService;
        this.emailConfirmationTokenService = emailConfirmationTokenService;
        this.emailService = emailService;
    }

    /**
     * Returns the page to request an email confirmation.
     *
     * @param error The error, should be given by other endpoints.
     * @param model The model of the template.
     * @return The page to render.
     */
    @GetMapping({"/request-confirmation", "/request-confirmation/"})
    public String emailConfirmationPage(
            @RequestParam(name = "error", required = false, defaultValue = "0") int error,
            Model model) {
        if (currentUserContext.userExists()) {
            return "redirect:/";
        }

        String errorMsg;
        switch (error) {
            case 0 -> errorMsg = "";
            case 1 -> errorMsg = "Account is already confirmed.";
            case 2 -> errorMsg = "Error confirming account.";
            case 3 -> errorMsg = "Email does not exist.";
            default -> errorMsg = "Invalid error code.";
        }
        model.addAttribute("error", errorMsg);
        return "requestActivationPage";
    }

    /**
     * Endpoint which starts the procedure of sending a confirmation email.
     *
     * @param email The email to confirm again.
     * @return The redirect to a template to render.
     */
    @PostMapping({"/request-confirmation", "/request-confirmation/"})
    public String emailConfirmationProcedure(@ModelAttribute(name = "email") String email) {
        if (currentUserContext.userExists()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              "User already has an activated account.");
        }

        Optional<Account> accountOpt = accountService.getAccountByEmail(email);

        if (accountOpt.isEmpty()) {
            return "redirect:/auth/request-confirmation?error=3";
        }

        Account account = accountOpt.get();
        if (account.emailConfirmed()) {
            return "redirect:/auth/request-confirmation?error=1";
        }

        try {
            String token = emailConfirmationTokenService.generateConfirmationTokenByEmail(email);
            emailService.sendConfirmationEmail(email, token);
        } catch (RuntimeException e) {
            return "redirect:/auth/request-confirmation?error=2";
        }

        return "redirect:/auth/confirm/";
    }
}
