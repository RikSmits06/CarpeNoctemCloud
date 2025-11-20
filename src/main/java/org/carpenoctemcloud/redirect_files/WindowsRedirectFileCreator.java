package org.carpenoctemcloud.redirect_files;

import org.carpenoctemcloud.remote_file.RemoteFile;

/**
 * Creates an .url file for Windows users.
 */
public class WindowsRedirectFileCreator implements RedirectFileCreator {

    /**
     * Default constructor so that only the {@link RedirectFileFactory} can create an instance.
     */
    WindowsRedirectFileCreator() {
    }
    
    @Override
    public String createFileContent(RemoteFile file) {
        return "[InternetShortcut]\n" + "URL=" + file.name() + "\n";
    }

    /**
     * Used to create the right extension of the file.
     *
     * @return The extension of the file with the dot. So for example ".url" or ".docx".
     */
    @Override
    public String getFileExtension() {
        return ".url";
    }
}
