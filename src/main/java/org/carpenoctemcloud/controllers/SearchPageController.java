package org.carpenoctemcloud.controllers;

import org.carpenoctemcloud.category.CategoryService;
import org.carpenoctemcloud.remote_file.RemoteFileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.carpenoctemcloud.configuration.ConfigurationConstants.MAX_FETCH_SIZE;

/**
 * Mapper for the search page located at "/search/".
 */
@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("/search")
public class SearchPageController {

    private final RemoteFileService fileService;
    private final CategoryService categoryService;

    /**
     * Constructor of the search page controller.
     *
     * @param fileService     The RemoteFileService used to interact with the RemoteFile table.
     * @param categoryService The CategoryService used to show the user the available categories to filter.
     */
    public SearchPageController(RemoteFileService fileService, CategoryService categoryService) {
        this.fileService = fileService;
        this.categoryService = categoryService;
    }

    /**
     * Main page for the search page.
     *
     * @param model      The model which contains the variables to be rendered.
     * @param query      The search query given to the user.
     * @param offset     The offset of the search query.
     * @param categoryID The category id to filter on.
     * @return The template of the search page with the found results.
     */
    @GetMapping({"", "/"})
    public String searchPage(Model model,
                             @RequestParam(required = false, defaultValue = "") String query,
                             @RequestParam(required = false, defaultValue = "0") Integer offset,
                             @RequestParam(required = false, defaultValue = "", name = "cat")
                             Integer categoryID) {
        offset = (offset > 0) ? offset : 0;

        model.addAttribute("query", query);
        model.addAttribute("offset", offset);
        model.addAttribute("maxFetchSize", MAX_FETCH_SIZE);
        model.addAttribute("results", fileService.searchRemoteFiles(query, offset, categoryID));
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("currentCategory", categoryID);
        model.addAttribute("totalFiles", fileService.getTotalFiles());

        return "searchPage";
    }
}