package com.nuevatel.bcf.service;

/**
 * Creates initialize and stop teh MediaService.
 *
 * @author Ariel Salazar
 */
public final class MediaServiceFactory {

    /**
     * Media service instance.
     */
    private static MediaService service;

    /**
     *
     * @param threadPoolSize Count of threads for the service.
     * @return MediaService instance
     */
    public synchronized MediaService start(Integer threadPoolSize) {
        if (threadPoolSize == null) {
            service = new MediaServiceImpl();
        } else {
            service = new MediaServiceImpl(threadPoolSize);
        }
        service.start();
        return service;
    }

    /**
     * Stop the service.
     */
    public synchronized void shutdown() {
        if (service == null) {
            return;
        }
        service.shutdown();
        service = null;
    }

    /**
     *
     * @return If the service was started returns an instance of MediaService, in other case returns <b>null</b>.
     */
    public synchronized MediaService get() {
        return service;
    }
}
