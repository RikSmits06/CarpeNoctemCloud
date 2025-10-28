package org.carpenoctemcloud.controllers;

import org.carpenoctemcloud.remoteFile.RemoteFileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.carpenoctemcloud.configuration.ConfigurationConstants.MAX_FETCH_SIZE;

@Controller
public class searchPageController {

    final RemoteFileService service;

    public searchPageController(RemoteFileService service) {
        this.service = service;
    }

    @GetMapping({"/search", "/search/"})
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