package org.carpenoctemcloud.redirect_files;

/**
 * The purpose of this interface is to create classes which handle the link file of a specific OS.
 * For instance, windows has .url files while macOS has .webloc files.
 */
public abstract class RedirectFileCreator {
    /**
     * Creates the content of a file to redirect towards the resource at the url.
     *
     * @param url The url to redirect the user to.
     * @return The contents of the file in a string.
     */
    abstract public String createFileContent(String url);

    /**
     * Used to create the right extension of the file.
     *
     * @return The extension of the file with the dot. So for example ".url" or ".docx".
     */
    abstract public String getFileExtension();

    /**
     * If it is wise to compress the file before sending it to the client.
     * This is used because some file extensions are renamed by the browser.
     * For instance, .url gets replaced with.url.download which some people can't rename correctly.
     *
     * @return True by default but can be overridden to false.
     */
    public boolean compressFile() {
        return true;
    }

    public final String RedirectFileCreatorName() {
        return this.getClass().getSimpleName();
    }
}
