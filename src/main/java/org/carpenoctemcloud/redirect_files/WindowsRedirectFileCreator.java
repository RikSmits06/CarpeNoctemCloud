package org.carpenoctemcloud.redirect_files;

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
    public String createFileContent(String url) {
        return "[InternetShortcut]\n" + "URL=" + url + "\n";
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
