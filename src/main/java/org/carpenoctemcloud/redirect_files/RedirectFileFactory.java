package org.carpenoctemcloud.redirect_files;

/**
 * Factory class which will return a {@link RedirectFileCreator} to create link files.
 */
public class RedirectFileFactory {
    private RedirectFileFactory() {
    }

    /**
     * Creates an object which can create the redirect files.
     * Can throw an exception if the platform is not supported but that should not happen.
     *
     * @param platform The platform for which to make the redirect file.
     * @return The {@link RedirectFileCreator} for the given platform.
     */
    public static RedirectFileCreator getFileCreator(RedirectFilePlatform platform) {
        switch (platform) {
            case MAC -> {
                return new MacRedirectFileCreator();
            }
            case WINDOWS -> {
                return new WindowsRedirectFileCreator();
            }
            case VLC -> {
                return new VlcRedirectFileCreator();
            }
            default -> throw new IllegalStateException("Unexpected value: " + platform);
        }
    }
}
