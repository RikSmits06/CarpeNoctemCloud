package org.carpenoctemcloud.controllers.account;

import org.carpenoctemcloud.auth.CurrentUserContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Controller located at /account used to show the profile page of the user.
 */
@Controller
@RequestMapping("/account")
@RequestScope
public class profilePageController {

    private final CurrentUserContext currentUserContext;

    /**
     * Creates a new controller for the profile page.
     *
     * @param currentUserContext The context of the current user needed to set an active user.
     */
    public profilePageController(CurrentUserContext currentUserContext) {
        this.currentUserContext = currentUserContext;
    }

    /**
     * Profile page for the user.
     *
     * @param model The model used by thymeleaf.
     * @return The name of the template to go to.
     */
    @GetMapping({"/profile", "/profile/"})
    public String profilePage(Model model) {

        if (!currentUserContext.userExists()) {
            return "redirect:/auth/login";
        }
        if (currentUserContext.getUser().isPresent()) {
            model.addAttribute("user", currentUserContext.getUser().get());
        }

        return "profilePage";
    }
}
