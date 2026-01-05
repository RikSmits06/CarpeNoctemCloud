package org.carpenoctemcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main class which runs the entire web server.
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class CarpeNoctemCloudApplication {
    private CarpeNoctemCloudApplication() {
    }

    /**
     * Main entry point for the server.
     *
     * @param args Arguments are directly passed to SpringApplication.run.
     */
    public static void main(String[] args) {
        SpringApplication.run(CarpeNoctemCloudApplication.class, args);
    }

}
