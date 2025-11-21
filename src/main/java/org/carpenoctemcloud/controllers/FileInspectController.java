package org.carpenoctemcloud.controllers;

import org.carpenoctemcloud.remote_file.RemoteFile;
import org.carpenoctemcloud.remote_file.RemoteFileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

/**
 * Collection of endpoints which handles sending a downloadable file to the client.
 * This downloadable file is what will redirect the client.
 */
@Controller
@SuppressWarnings("SameReturnValue")
@RequestMapping("/file")
public class FileInspectController {

    private final RemoteFileService fileService;

    /**
     * Creates a new controller instance.
     *
     * @param fileService The file service to query the files through.
     */
    public FileInspectController(RemoteFileService fileService) {
        this.fileService = fileService;
    }

    /**
     * The page where you can inspect/view a specific file.
     *
     * @param model The model of the Thymeleaf ssr.
     * @param id    The id of the RemoteFile.
     * @return The template of the fileInspectPage.
     */
    @GetMapping({"/{id}", "/{id}/"})
    public String fileInspectPage(Model model, @PathVariable int id) {
        Optional<RemoteFile> file = fileService.getRemoteFileByID(id);
        //String server = file.get().downloadURL().replaceFirst("^file:/*", "").split("/")[0];


        model.addAttribute("resultFile", file.get());
        model.addAttribute("serverURL", "placeholder.nl");

        return "fileInspectPage";
    }
}
