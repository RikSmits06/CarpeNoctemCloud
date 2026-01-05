package org.carpenoctemcloud.controllers.search;

import java.util.Optional;
import org.carpenoctemcloud.account.Account;
import org.carpenoctemcloud.auth.CurrentUserContext;
import org.carpenoctemcloud.category.Category;
import org.carpenoctemcloud.category.CategoryService;
import org.carpenoctemcloud.remote_file.RemoteFile;
import org.carpenoctemcloud.remote_file.RemoteFileService;
import org.carpenoctemcloud.server.Server;
import org.carpenoctemcloud.starred_remote_files.StarredRemoteFilesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;

/**
 * Collection of endpoints which handles sending a downloadable file to the client.
 * This downloadable file is what will redirect the client.
 */
@Controller
@SuppressWarnings("SameReturnValue")
@RequestMapping("/file")
@RequestScope
public class FileInspectController {
    private final Logger logger = LoggerFactory.getLogger(FileInspectController.class);
    private final RemoteFileService fileService;
    private final CategoryService categoryService;
    private final StarredRemoteFilesService starredRemoteFilesService;
    private final CurrentUserContext currentUserContext;

    /**
     * Creates a new controller instance.
     *
     * @param fileService               The file service to query the files through.
     * @param categoryService           Service used to list categories.
     * @param currentUserContext        The context of the current user. Used to check if they starred a file.
     * @param starredRemoteFilesService Used to check if the current user starred a file.
     */
    public FileInspectController(RemoteFileService fileService, CategoryService categoryService,
                                 StarredRemoteFilesService starredRemoteFilesService,
                                 CurrentUserContext currentUserContext) {
        this.fileService = fileService;
        this.categoryService = categoryService;
        this.starredRemoteFilesService = starredRemoteFilesService;
        this.currentUserContext = currentUserContext;
    }

    /**
     * The page where you can inspect/view a specific file.
     *
     * @param model The model of the Thymeleaf ssr.
     * @param id    The id of the RemoteFile.
     * @return The template of the fileInspectPage.
     */
    @GetMapping({"/{id}", "/{id}/"})
    public String fileInspectPage(Model model, @PathVariable long id) {
        Optional<RemoteFile> fileOpt = fileService.getRemoteFileByID(id);

        if (fileOpt.isEmpty()) {
            logger.warn("Invalid file accessed with id={}.", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File ID does not exist.");
        }

        RemoteFile file = fileOpt.get();
        model.addAttribute("category", null);
        if (file.categoryID().isPresent()) {
            Optional<Category> categoryOpt = categoryService.getCategory(file.categoryID().get());
            if (categoryOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                                  "Category could not be found but file has category.");
            }
            model.addAttribute("category", categoryOpt.get());
        }
        Server server = fileService.getServerOfFile(id);

        model.addAttribute("resultFile", file);
        model.addAttribute("server", server);

        // Check if it is starred.
        Optional<Account> accountOpt = currentUserContext.getUser();
        accountOpt.ifPresent(account -> model.addAttribute("isStarred",
                                                           starredRemoteFilesService.isFileStarred(
                                                                   account.id(), id)));

        return "fileInspectPage";
    }

    /**
     * Toggles if a file has been starred.
     *
     * @param id The id of the file to toggle.
     * @return Redirect back to the files' inspect page.
     */
    @PostMapping({"{id}/star", "{id}/star/"})
    public String toggleStar(@PathVariable long id) {
        Optional<Account> userOpt = currentUserContext.getUser();
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              "User is not logged in so cannot star file.");
        }
        Account user = userOpt.get();
        if (starredRemoteFilesService.isFileStarred(user.id(), id)) {
            starredRemoteFilesService.unStarFile(user.id(), id);
        } else {
            starredRemoteFilesService.starFile(user.id(), id);
        }
        return "redirect:/file/" + id;
    }
}
