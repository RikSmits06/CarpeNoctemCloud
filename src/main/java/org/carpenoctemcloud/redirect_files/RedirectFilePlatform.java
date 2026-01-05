package org.carpenoctemcloud.redirect_files;

/**
 * Enum containing all the platforms we support creating redirect files for.
 */
public enum RedirectFilePlatform {
    /**
     * The Apple Mac operating system.
     */
    MAC,
    /**
     * The Microsoft Windows operating system.
     */
    WINDOWS,
    /**
     * Used to open the file in VLC using a PLS file format.
     */
    VLC
}
