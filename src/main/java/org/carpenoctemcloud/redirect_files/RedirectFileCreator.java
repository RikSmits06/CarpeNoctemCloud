package org.carpenoctemcloud.redirect_files;

/**
 * The purpose of this interface is to create classes which handle the link file of a specific OS.
 * For instance, windows has .url files while macOS has .webloc files.
 */
public interface RedirectFileCreator {
    /**
     * Creates the content of a file to redirect towards the resource at the url.
     *
     * @param url The url to redirect the user to.
     * @return The contents of the file in a string.
     */
    String createFileContent(String url);

    /**
     * Used to create the right extension of the file.
     *
     * @return The extension of the file with the dot. So for example ".url" or ".docx".
     */
    String getFileExtension();

    default boolean compressFile() {
        return true;
    }
}
