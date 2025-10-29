package org.carpenoctemcloud.redirectFiles;

public class WindowsRedirectFileCreator implements RedirectFileCreator {
    @Override
    public String createFileContent(String url) {
        return "[InternetShortcut]\n" + "URL=" + url + "\n";
    }
}
