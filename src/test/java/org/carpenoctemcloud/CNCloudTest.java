package org.carpenoctemcloud;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@CNCloudTestNoDatabase
@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.EMBEDDED,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_CLASS)
@Retention(RetentionPolicy.RUNTIME)
public @interface CNCloudTest {
}
