package org.carpenoctemcloud.controllers;

import java.util.Optional;
import org.carpenoctemcloud.redirectFiles.RedirectFileCreator;
import org.carpenoctemcloud.redirectFiles.RedirectFileFactory;
import org.carpenoctemcloud.redirectFiles.RedirectFilePlatform;
import org.carpenoctemcloud.remoteFile.RemoteFile;
import org.carpenoctemcloud.remoteFile.RemoteFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/redirect-file")
public class RedirectFileController {

    private final RemoteFileService fileService;
    private final Logger logger = LoggerFactory.getLogger(RedirectFileController.class);

    public RedirectFileController(RemoteFileService fileService) {
        this.fileService = fileService;
    }

    /**
     * The redirect-file created for the specific id of a RemoteFile.
     * Is meant to be downloaded from not seen by the user.
     *
     * @param model The model of the Thymeleaf ssr.
     * @param id    The id of the RemoteFile.
     * @return The downloadable file or a 400 error if the id doesn't match a RemoteFile.
     */
    @GetMapping({"/{id}", "/{id}/"})
    public String universalFileDownload(Model model, @PathVariable int id) {
        Optional<RemoteFile> file = fileService.getRemoteFileByID(id);

        if (file.isEmpty()) {
            logger.warn("Invalid redirect-file accessed with id={}.", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File ID does not exist.");
        }

        model.addAttribute("resultFile", file.get());

        return "redirectFile";
    }

    /**
     * The redirect-file created for the specific id of a RemoteFile.
     * Is meant to be downloaded from not seen by the user.
     *
     * @param platform The platform to build the file for.
     * @param id       The id of the RemoteFile.
     * @return The downloadable file or a 400 error if the id doesn't match a RemoteFile.
     */
    @GetMapping({"/{platform}/{id}", "/{platform}/{id}/"})
    public ResponseEntity<String> downloadFile(@PathVariable RedirectFilePlatform platform,
                                               @PathVariable int id) {
        Optional<RemoteFile> file = fileService.getRemoteFileByID(id);
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given file does not exist.");
        }

        RedirectFileCreator fileCreator = RedirectFileFactory.getFileCreater(platform);
        String content = fileCreator.createFileContent(file.get().downloadURL());
        return ResponseEntity.ok().body(content);
    }
}
