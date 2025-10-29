package org.carpenoctemcloud.controllers;

import java.util.Optional;
import org.carpenoctemcloud.remoteFile.RemoteFile;
import org.carpenoctemcloud.remoteFile.RemoteFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import static org.carpenoctemcloud.configuration.ConfigurationConstants.MAX_FETCH_SIZE;

/**
 * Mapper for the search page located at "/search/".
 */
@Controller
@RequestMapping("/search")
public class searchPageController {

    final RemoteFileService service;
    final Logger logger = LoggerFactory.getLogger(searchPageController.class);

    /**
     * Constructor of the search page controller.
     *
     * @param service The RemoteFileService used to interact with the RemoteFile table.
     */
    public searchPageController(RemoteFileService service) {
        this.service = service;
    }

    /**
     * Main page for the search page.
     *
     * @param model  The model which contains the variables to be rendered.
     * @param query  The search query given to the user.
     * @param offset The offset of the search query.
     * @return The template of the search page with the found results.
     */
    @GetMapping({"", "/"})
    public String searchPage(Model model,
                             @RequestParam(required = false, defaultValue = "") String query,
                             @RequestParam(required = false, defaultValue = "0") Integer offset) {
        offset = (offset > 0) ? offset : 0;

        model.addAttribute("query", query);
        model.addAttribute("offset", offset);
        model.addAttribute("maxFetchSize", MAX_FETCH_SIZE);
        model.addAttribute("results", service.searchRemoteFiles(query, offset));

        return "searchPage";
    }
}