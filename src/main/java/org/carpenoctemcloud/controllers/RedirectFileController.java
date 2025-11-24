package org.carpenoctemcloud.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.carpenoctemcloud.redirect_files.RedirectFileCreator;
import org.carpenoctemcloud.redirect_files.RedirectFileFactory;
import org.carpenoctemcloud.redirect_files.RedirectFilePlatform;
import org.carpenoctemcloud.remote_file.RemoteFile;
import org.carpenoctemcloud.remote_file.RemoteFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/redirect-file")
public class RedirectFileController {

    private final RemoteFileService fileService;
    private final Logger logger = LoggerFactory.getLogger(RedirectFileController.class);

    /**
     * Creates a new controller instance.
     *
     * @param fileService The file service to query the files through.
     */
    public RedirectFileController(RemoteFileService fileService) {
        this.fileService = fileService;
    }

    /**
     * The redirect-file created for the specific id of a RemoteFile.
     * Is meant to be downloaded from not seen by the user.
     *
     * @param platform The platform to build the file for.
     * @param id       The id of the RemoteFile.
     * @return The downloadable file or a 404 error if the id doesn't match a RemoteFile.
     */
    @GetMapping({"/{platform}/{id}", "/{platform}/{id}/"})
    public ResponseEntity<InputStreamResource> downloadFile(
            @PathVariable RedirectFilePlatform platform, @PathVariable int id) {
        Optional<RemoteFile> fileOpt = fileService.getRemoteFileByID(id);
        if (fileOpt.isEmpty()) {
            logger.warn("Invalid file accessed with id={}.", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Given file does not exist.");
        }

        RemoteFile file = fileOpt.get();
        RedirectFileCreator fileCreator = RedirectFileFactory.getFileCreator(platform);
        String content = fileCreator.createFileContent(fileService.getDownloadURL(file.id()));

        ByteArrayInputStream output;

        if (fileCreator.compressFile()) {
            // Trying to zip the redirect file so that it won't add weird extension while downloading.
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (ZipOutputStream zipStream = new ZipOutputStream(byteArrayOutputStream)) {
                zipStream.putNextEntry(new ZipEntry(file.name() + fileCreator.getFileExtension()));
                zipStream.write(content.getBytes());
                zipStream.closeEntry();
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                                  "Error creating zip file.");
            }
            output = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } else {
            output = new ByteArrayInputStream(content.getBytes());
        }


        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                                          "attachment; filename=" + file.name() +
                                                  (fileCreator.compressFile() ? ".zip" :
                                                          fileCreator.getFileExtension()))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(output));
    }
}
