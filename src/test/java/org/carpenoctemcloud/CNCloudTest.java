package org.carpenoctemcloud;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Composite annotation which tells spring boot this is a test which needs access to the beans.
 * It will also generate a mock database which resets before each test method.
 * Also loads the test profile using {@link CNCloudTestNoDatabase}.
 */
@CNCloudTestNoDatabase
@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.EMBEDDED,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CNCloudTest {
}
