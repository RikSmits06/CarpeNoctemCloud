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

@Controller
@RequestMapping("/search")
public class searchPageController {

    final RemoteFileService service;
    final Logger logger = LoggerFactory.getLogger(searchPageController.class);

    public searchPageController(RemoteFileService service) {
        this.service = service;
    }

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

    @GetMapping({"/redirect-file/{id}", "/redirect-file/{id}/"})
    public String downloadFile(Model model, @PathVariable int id) {
        Optional<RemoteFile> file = service.getRemoteFileByID(id);

        if (file.isEmpty()) {
            logger.warn("Invalid redirect-file accessed with id={}.", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File ID does not exist.");
        }

        model.addAttribute("resultFile", file.get());

        return "redirectFile";
    }
}