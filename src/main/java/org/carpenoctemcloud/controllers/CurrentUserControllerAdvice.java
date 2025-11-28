package org.carpenoctemcloud.controllers;

import org.carpenoctemcloud.auth.CurrentUserContext;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.annotation.RequestScope;

@ControllerAdvice
@RequestScope
public class CurrentUserControllerAdvice {

    private final CurrentUserContext currentUserContext;

    public CurrentUserControllerAdvice(CurrentUserContext currentUserContext) {
        this.currentUserContext = currentUserContext;
    }

    @ModelAttribute
    public void currentUser(Model model) {
        if (currentUserContext.getUser().isPresent()) {
            model.addAttribute("currentUser", currentUserContext.getUser().get());
        } else {
            model.addAttribute("currentUser", null);
        }
    }
}
