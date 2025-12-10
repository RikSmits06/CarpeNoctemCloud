package org.carpenoctemcloud.controllers.file_system;

import java.util.List;
import java.util.Optional;
import org.carpenoctemcloud.category.CategoryService;
import org.carpenoctemcloud.directory.Directory;
import org.carpenoctemcloud.directory.DirectoryService;
import org.carpenoctemcloud.remote_file.RemoteFile;
import org.carpenoctemcloud.remote_file_system.RemoteFileSystemService;
import org.carpenoctemcloud.server.Server;
import org.carpenoctemcloud.server.ServerService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

/**
 * The controller used to map out the file system which has been cached.
 */
@Controller
@RequestMapping("/file-system")
public class FileSystemController {


    private final RemoteFileSystemService remoteFileSystemService;
    private final DirectoryService directoryService;

    private final CategoryService categoryService;
    private final ServerService serverService;

    /**
     * Creates a new controller.
     *
     * @param remoteFileSystemService Service used to retrieve files in the current directory.
     * @param directoryService        Service used to get the child directories and info about the current directory.
     * @param categoryService         Used to get the category to label files correctly in the view.
     * @param serverService           The service used to find all servers in the database.
     */
    public FileSystemController(RemoteFileSystemService remoteFileSystemService,
                                DirectoryService directoryService, CategoryService categoryService,
                                ServerService serverService) {
        this.remoteFileSystemService = remoteFileSystemService;
        this.directoryService = directoryService;
        this.categoryService = categoryService;
        this.serverService = serverService;
    }

    /**
     * Endpoint which will list out all the servers.
     *
     * @param model Used to pass arguments to the template.
     * @return The page showing all the servers.
     */
    @GetMapping({"/"})
    public String serversExplorerPage(Model model) {
        model.addAttribute("servers", serverService.getServers());
        return "serverSystemPage";
    }

    /**
     * Shows the page containing the top-level directory of the server.
     *
     * @param id    The id of the server.
     * @param model Used to render the template.
     * @return The page with all the top-level directory.
     */
    @GetMapping({"/server/{id}", "/server/{id}/"})
    public String serverDirectoryPage(@PathVariable(name = "id") long id, Model model) {
        Optional<Server> serverOpt = serverService.getServerByID(id);

        if (serverOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Server does not exist.");
        }

        List<Directory> directories = remoteFileSystemService.getTopLevelDirectories(id);

        model.addAttribute("server", serverOpt.get());
        model.addAttribute("directories", directories);
        return "serverDirectoriesPage";
    }

    /**
     * Page which shows a directories content. Including files and subdirectories.
     *
     * @param id    The id of the directory.
     * @param model The model used to render the template.
     * @return A rendered template showing directory layout.
     */
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
        if (!files.isEmpty()) {
            model.addAttribute("categories", categoryService.getAllCategories());
        }
        return "directorySystemPage";
    }
}
