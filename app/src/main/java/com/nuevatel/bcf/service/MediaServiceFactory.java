package com.nuevatel.bcf.service;

/**
 * Created by asalazar on 6/14/15.
 */
public final class MediaServiceFactory {

    private static MediaService service;



    private MediaServiceFactory() {
        // No op. Used to prevent instantiation.
    }

    public synchronized static MediaService get() {
        if (service == null) {
            service = new MediaServiceImpl();
        }

        return service;
    }
}
