package org.carpenoctemcloud;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.EMBEDDED,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_CLASS)
@Retention(RetentionPolicy.RUNTIME)
public @interface CNCloudTest {
}
