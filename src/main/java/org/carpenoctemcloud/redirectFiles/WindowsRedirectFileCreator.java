package org.carpenoctemcloud.redirectFiles;

public class WindowsRedirectFileCreator implements RedirectFileCreator {

    /**
     * Default constructor so only the factory can create new Redirector.
     */
    WindowsRedirectFileCreator() {
    }

    /**
     * Creates the content of a file to redirect towards the resource at the url.
     *
     * @param url The url to redirect the user to.
     * @return The contents of the file in a string.
     */
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
