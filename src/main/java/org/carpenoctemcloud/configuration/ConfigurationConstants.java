package org.carpenoctemcloud.configuration;

/**
 * Code constants for configuring how it is run.
 */
public class ConfigurationConstants {

    /**
     * The time it should take for the cleanup task to delete this file as it is considered outdated.
     * This means that if a cleanup cycle is triggered, and the file hasn't been indexed in 4 days,
     * it will be deleted.
     */
    public static final int MAX_AGE_CACHE_IN_DAYS = 4;
    /**
     * Limits how many films can be queried at once.
     */
    public static final int MAX_FETCH_SIZE = 20;

    /**
     * Limits the amount of recently downloaded films that will be displayed.
     */
    public static final int MAX_FETCH_SIZE_HISTORY = 10;

    private ConfigurationConstants() {
    }
}
