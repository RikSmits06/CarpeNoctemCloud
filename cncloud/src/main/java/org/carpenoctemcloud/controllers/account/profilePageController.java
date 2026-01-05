package org.carpenoctemcloud.controllers.account;

import org.carpenoctemcloud.account.Account;
import org.carpenoctemcloud.auth.CurrentUserContext;
import org.carpenoctemcloud.category.CategoryService;
import org.carpenoctemcloud.download_history.DownloadHistoryService;
import org.carpenoctemcloud.starred_remote_files.StarredRemoteFilesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

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
    private final DownloadHistoryService downloadHistoryService;

    /**
     * Creates a new controller for the profile page.
     *
     * @param currentUserContext The context of the current user needed to set an active user.
     */
    public profilePageController(CurrentUserContext currentUserContext, StarredRemoteFilesService starredRemoteFilesService, CategoryService categoryService, DownloadHistoryService downloadHistoryService) {
        this.currentUserContext = currentUserContext;
        this.starredRemoteFilesService = starredRemoteFilesService;
        this.categoryService = categoryService;
        this.downloadHistoryService = downloadHistoryService;
    }

    /**
     * Profile page for the user.
     *
     * @param model The model used by thymeleaf.
     * @return The name of the template to go to.
     */
    @GetMapping({"/profile", "/profile/"})
    public String profilePage(Model model) {
        Optional<Account> currentUserOpt = currentUserContext.getUser();
        if (currentUserOpt.isEmpty()) {
            return "redirect:/auth/login";
        }
        Account currentUser = currentUserOpt.get();
        int currentUserID = currentUser.id();

        model.addAttribute("starredFiles", starredRemoteFilesService.getStarredFiles(currentUserID));
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("recentDownloads", downloadHistoryService.getRecentHistory(currentUserID));


        return "profilePage";
    }
}
