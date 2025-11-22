package org.carpenoctemcloud.redirect_files;

import java.util.Objects;

/**
 * Creates file content for vlc files. They open directly in vlc.
 */
public class VlcRedirectFileCreator implements RedirectFileCreator {

    /**
     * Creates a new VlcRedirectFileCreator, it does not use any dependencies.
     */
    public VlcRedirectFileCreator() {

    }

    /**
     * Creates the content of a file to redirect towards the resource at the url.
     *
     * @param url The url to redirect the user to.
     * @return The contents of the file in a string.
     */
    @Override
    public String createFileContent(String url) {
        String[] split = url.split("://", 2);
        String protocol = split[0];

        // When using file:// with VLC, it does not work as it looks for a local file.
        // If we use smb, it looks on the network.
        if (Objects.equals(protocol, "file")) {
            protocol = "smb";
        }

        url = protocol + "://" + "anonymous:1234@" + split[1];
        return "[playlist]\n" + "File1=" + url + "\nNumberOfEntries=1";
    }

    /**
     * Used to create the right extension of the file.
     *
     * @return The extension of the file with the dot. So for example ".url" or ".docx".
     */
    @Override
    public String getFileExtension() {
        return ".vlc";
    }

    @Override
    public boolean compressFile() {
        return false;
    }
}
