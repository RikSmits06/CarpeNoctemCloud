package org.carpenoctemcloud.controllers.account;

import org.carpenoctemcloud.auth.CurrentUserContext;
import org.carpenoctemcloud.category.CategoryService;
import org.carpenoctemcloud.starred_remote_files.StarredRemoteFilesService;
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
    private final StarredRemoteFilesService starredRemoteFilesService;
    private final CategoryService categoryService;

    /**
     * Creates a new controller for the profile page.
     *
     * @param currentUserContext The context of the current user needed to set an active user.
     */
    public profilePageController(CurrentUserContext currentUserContext, StarredRemoteFilesService starredRemoteFilesService, CategoryService categoryService) {
        this.currentUserContext = currentUserContext;
        this.starredRemoteFilesService = starredRemoteFilesService;
        this.categoryService = categoryService;
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
            if (!currentUserContext.getUser().get().emailConfirmed()) {
                // can this even happen?
                return "redirect:/auth/request-confirmation";
            }
            model.addAttribute("user", currentUserContext.getUser().get());
            model.addAttribute("starredFiles", starredRemoteFilesService.getStarredFiles(currentUserContext.getUser().get().id()));
            model.addAttribute("categories", categoryService.getAllCategories());
        }

        return "profilePage";
    }
}
