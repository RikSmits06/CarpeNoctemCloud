package org.carpenoctemcloud;


import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Retention(RetentionPolicy.RUNTIME)
public @interface CNCloudTestNoDatabase {
}
