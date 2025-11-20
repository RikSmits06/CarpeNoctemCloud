package org.carpenoctemcloud.redirect_files;

import org.carpenoctemcloud.remote_file.RemoteFile;

/**
 * Class which implements the RedirectFileCreator interface.
 * Is used to make redirect files for the Mac operating system.
 */
public class MacRedirectFileCreator implements RedirectFileCreator {

    /**
     * Default constructor as the class should only be made through {@link RedirectFileFactory}.
     */
    MacRedirectFileCreator() {
    }

    @Override
    public String createFileContent(RemoteFile file) {
        return """
                <?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "https://www.apple.com/DTDs/PropertyList-1.0.dtd">
                <plist version="1.0">
                <dict>
                <key>URL</key>
                <string>""" + file.name() + "</string></dict></plist>";
    }

    /**
     * Used to create the right extension of the file.
     *
     * @return The extension of the file with the dot. So for example ".url" or ".docx".
     */
    @Override
    public String getFileExtension() {
        return ".webloc";
    }
}
