package org.carpenoctemcloud.shell_commands;

import java.time.Duration;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DurationFormat;
import org.springframework.format.datetime.standard.DurationFormatterUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class ScanServerCommand {

    @ShellMethod(key = "scanSMB", value = "Scans an SMB server.")
    public String ScanSMB(@ShellOption(value = "Url of the server to index.") String url) {
        LocalDateTime start = LocalDateTime.now();

        LocalDateTime end = LocalDateTime.now();
        Duration duration = Duration.between(start, end);
        return DurationFormatterUtils.print(duration, DurationFormat.Style.COMPOSITE);
    }
}
