package org.carpenoctemcloud.controllers;

import org.carpenoctemcloud.auth.CurrentUserContext;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Advisor responsible to add the account of the current user as a global variable in the templates.
 */
@ControllerAdvice
@RequestScope
public class CurrentUserControllerAdvice {

    private final CurrentUserContext currentUserContext;

    /**
     * Creates a new controller advisor.
     *
     * @param currentUserContext The context of the current user.
     */
    public CurrentUserControllerAdvice(CurrentUserContext currentUserContext) {
        this.currentUserContext = currentUserContext;
    }

    /**
     * Sets the current user as a global variable called {@code currentUser}.
     *
     * @param model The model of the template.
     */
    @ModelAttribute
    public void currentUser(Model model) {
        if (currentUserContext.getUser().isPresent()) {
            model.addAttribute("currentUser", currentUserContext.getUser().get());
        } else {
            model.addAttribute("currentUser", null);
        }
    }
}
