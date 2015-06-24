package com.nuevatel.bcf.service;

/**
 * Creates, start and shutdown MediaService
 *
 * @author Ariel Salazar
 */
public final class MediaServiceFactory {

    private static MediaService service;

    public MediaServiceFactory() {
        // No op. Used to prevent instantiation.
    }

    public synchronized MediaService get() {
        return service;
    }

    public synchronized void start(int threadPoolSize) {
        service = new MediaServiceImpl(threadPoolSize);
        service.start();
    }

    public synchronized void shutdown() {
        if (service == null) {
            return;
        }
        service.shutdown();
    }
}
