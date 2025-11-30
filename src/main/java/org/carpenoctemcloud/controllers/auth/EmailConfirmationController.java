package org.carpenoctemcloud.controllers.auth;

import org.carpenoctemcloud.auth.CurrentUserContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.RequestScope;

@Controller
@RequestScope
@RequestMapping("/auth")
public class EmailConfirmationController {

    private final CurrentUserContext currentUserContext;

    public EmailConfirmationController(CurrentUserContext currentUserContext) {
        this.currentUserContext = currentUserContext;
    }

    @GetMapping({"/confirm", "/confirm/"})
    public String confirmEmailPage() {
        if (currentUserContext.userExists()) {
            return "redirect:/";
        }

        return "emailConfirmPage";
    }
}
