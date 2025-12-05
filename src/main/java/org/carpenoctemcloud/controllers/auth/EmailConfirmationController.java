package org.carpenoctemcloud.controllers.auth;

import org.carpenoctemcloud.auth.CurrentUserContext;
import org.carpenoctemcloud.email_confirmation.EmailConfirmationTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller which handles the outside facing interface of confirming emails.
 */
@Controller
@RequestScope
@RequestMapping("/auth")
public class EmailConfirmationController {

    private final CurrentUserContext currentUserContext;
    private final EmailConfirmationTokenService emailConfirmationTokenService;

    /**
     * Creates a new controller with the needed services.
     *
     * @param currentUserContext            The context holding the current user to prevent them from coming here.
     * @param emailConfirmationTokenService Token service to check if received token exists.
     */
    public EmailConfirmationController(CurrentUserContext currentUserContext,
                                       EmailConfirmationTokenService emailConfirmationTokenService) {
        this.currentUserContext = currentUserContext;
        this.emailConfirmationTokenService = emailConfirmationTokenService;
    }

    /**
     * Endpoint which returns to confirm frontend page.
     *
     * @param error The error code if something goes wrong. Should be set by other endpoints.
     * @param model The model for the template.
     * @return The template of the confirm page or a redirect if the user is already logged in.
     */
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

    /**
     * Endpoint to confirm the given token. Will confirm the email that the token maps to.
     *
     * @param token The token used to confirm an email.
     * @return A redirect back to a frontend page depending on failure or success.
     */
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
