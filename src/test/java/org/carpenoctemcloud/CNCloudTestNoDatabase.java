package org.carpenoctemcloud;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Composite annotation for use above test classes.
 * Will tell Spring Boot to load the bean context.
 * Also loads the test profile.
 */
@SpringBootTest
@ActiveProfiles("test")
@Retention(RetentionPolicy.RUNTIME)
public @interface CNCloudTestNoDatabase {
}
