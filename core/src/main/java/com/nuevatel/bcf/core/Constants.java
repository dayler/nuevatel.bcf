package com.nuevatel.bcf.core;

/**
 * Common constants used through application.
 */
public final class Constants {

    /**
     * Default period time to report.
     */
    public final static int WATCH_ARCG0 = 120*10;

    private Constants() {
        // No op. Used to prevent instantiation.
    }
    
    public final static String LOCK_NAME = "lock";
    
    public final static String UNLOCK_NAME = "unlock";
}
