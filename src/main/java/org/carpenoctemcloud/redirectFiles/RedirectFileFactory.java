package org.carpenoctemcloud.redirectFiles;

public class RedirectFileFactory {
    public static RedirectFileCreator getFileCreater(RedirectFilePlatform platform) {
        switch (platform) {
            case WINDOWS -> {
                return new WindowsRedirectFileCreator();
            }
            default -> throw new IllegalStateException("Unexpected value: " + platform);
        }
    }
}
