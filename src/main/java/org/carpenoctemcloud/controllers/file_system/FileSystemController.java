package org.carpenoctemcloud.controllers.file_system;

import org.carpenoctemcloud.category.CategoryService;
import org.carpenoctemcloud.directory.Directory;
import org.carpenoctemcloud.directory.DirectoryService;
import org.carpenoctemcloud.remote_file.RemoteFile;
import org.carpenoctemcloud.remote_file_system.RemoteFileSystemService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/file-system")
public class FileSystemController {


    private final RemoteFileSystemService remoteFileSystemService;
    private final DirectoryService directoryService;

    private final CategoryService categoryService;

    public FileSystemController(RemoteFileSystemService remoteFileSystemService,
                                DirectoryService directoryService,
                                CategoryService categoryService) {
        this.remoteFileSystemService = remoteFileSystemService;
        this.directoryService = directoryService;
        this.categoryService = categoryService;
    }

    @GetMapping({"/"})
    public String serversExplorerPage(Model model) {
        model.addAttribute("servers", remoteFileSystemService.getServers());
        return "serverSystemPage";
    }

    @GetMapping({"/server/{id}", "/server/{id}/"})
    public String serverDirectoryPage(@PathVariable(name = "id") long id, Model model) {
        List<Directory> directories = remoteFileSystemService.getTopLevelDirectories(id);
        model.addAttribute("directories", directories);
        model.addAttribute("server", remoteFileSystemService.getServer(id));
        return "serverDirectoriesPage";
    }

    @GetMapping({"/directory/{id}", "/directory/{id}/"})
    public String directoryPage(@PathVariable(name = "id") long id, Model model) {
        Optional<Directory> currentDirectoryOpt = directoryService.getDirectory(id);

        if (currentDirectoryOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Directory does not exist.");
        }

        Directory currentDirectory = currentDirectoryOpt.get();
        List<Directory> subDirectories = remoteFileSystemService.getSubDirectories(id);
        List<RemoteFile> files = remoteFileSystemService.getRemoteFilesInDirectory(id);

        model.addAttribute("currentDirectory", currentDirectory);
        model.addAttribute("subDirectories", subDirectories);
        model.addAttribute("files", files);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "directorySystemPage";
    }
}
