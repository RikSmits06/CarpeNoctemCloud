package org.carpenoctemcloud.controllers.search;

import java.util.Optional;
import org.carpenoctemcloud.category.Category;
import org.carpenoctemcloud.category.CategoryService;
import org.carpenoctemcloud.remote_file.RemoteFile;
import org.carpenoctemcloud.remote_file.RemoteFileService;
import org.carpenoctemcloud.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

/**
 * Collection of endpoints which handles sending a downloadable file to the client.
 * This downloadable file is what will redirect the client.
 */
@Controller
@SuppressWarnings("SameReturnValue")
@RequestMapping("/file")
public class FileInspectController {
    private final Logger logger = LoggerFactory.getLogger(FileInspectController.class);
    private final RemoteFileService fileService;
    private final CategoryService categoryService;

    /**
     * Creates a new controller instance.
     *
     * @param fileService The file service to query the files through.
     */
    public FileInspectController(RemoteFileService fileService, CategoryService categoryService) {
        this.fileService = fileService;
        this.categoryService = categoryService;
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

        return "fileInspectPage";
    }
}
