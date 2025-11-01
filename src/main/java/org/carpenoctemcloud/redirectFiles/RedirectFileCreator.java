package org.carpenoctemcloud.redirectFiles;

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
}
