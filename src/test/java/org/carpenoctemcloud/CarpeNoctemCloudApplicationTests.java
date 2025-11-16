package org.carpenoctemcloud;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellRunner;

@SpringBootTest(properties = {"spring.shell.interactive.enabled=false", "spring.shell.script.enabled=false"})
class CarpeNoctemCloudApplicationTests {

    /**
     * Test is supposed to be empty to test the loading of the apps context.
     */
    @Test
    @SuppressWarnings("empty")
    void contextLoads() {
    }

}
